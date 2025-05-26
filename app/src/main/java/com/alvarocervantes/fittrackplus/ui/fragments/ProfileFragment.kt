package com.alvarocervantes.fittrackplus.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.alvarocervantes.fittrackplus.R
import com.alvarocervantes.fittrackplus.data.database.DatabaseProvider
import com.alvarocervantes.fittrackplus.data.firebase.FirebaseRepository
import com.alvarocervantes.fittrackplus.viewmodel.RoutineViewModel
import com.alvarocervantes.fittrackplus.viewmodel.TrainingHistoryViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: androidx.activity.result.ActivityResultLauncher<Intent>
    private lateinit var containerProfileLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        containerProfileLayout = view.findViewById(R.id.containerProfile)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnSuccessListener {
                    Toast.makeText(requireContext(), "✅ Sesión iniciada", Toast.LENGTH_SHORT).show()
                    actualizarVista()
                }
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), "❌ Error con Google: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        actualizarVista()
        return view
    }

    private fun actualizarVista() {
        containerProfileLayout.removeAllViews()

        val user = auth.currentUser

        if (user != null) {
            mostrarInfoUsuario(user)
            mostrarBotonCerrarSesion()
            mostrarBotonesBackup()
        } else {
            mostrarFormularioLogin()
        }

        mostrarSeccionAcercaDe()
    }

    private fun mostrarInfoUsuario(user: FirebaseUser) {
        val avatar = ImageView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(180, 180).apply {
                gravity = Gravity.CENTER_HORIZONTAL
                bottomMargin = 16
            }
            Glide.with(this)
                .load(user.photoUrl)
                .circleCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(this)
        }

        val nombre = TextView(requireContext()).apply {
            text = user.displayName ?: user.email ?: "Usuario"
            textSize = 18f
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(0, 16, 0, 32)
        }

        containerProfileLayout.addView(avatar)
        containerProfileLayout.addView(nombre)
    }

    private fun mostrarFormularioLogin() {
        val emailInput = EditText(requireContext()).apply {
            hint = "Correo electrónico"
        }

        val passwordInput = EditText(requireContext()).apply {
            hint = "Contraseña"
            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        val loginButton = Button(requireContext()).apply {
            text = "Iniciar sesión"
            setOnClickListener {
                val email = emailInput.text.toString()
                val password = passwordInput.text.toString()
                if (email.isNotBlank() && password.length >= 6) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "✅ Sesión iniciada", Toast.LENGTH_SHORT).show()
                            actualizarVista()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "❌ Error al iniciar sesión: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

        val registerButton = Button(requireContext()).apply {
            text = "Crear cuenta"
            setOnClickListener {
                val email = emailInput.text.toString()
                val password = passwordInput.text.toString()
                if (email.isNotBlank() && password.length >= 6) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "✅ Cuenta creada", Toast.LENGTH_SHORT).show()
                            actualizarVista()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "❌ Error al crear cuenta: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

        val googleButton = Button(requireContext()).apply {
            text = "Iniciar sesión con Google"
            setOnClickListener {
                googleSignInLauncher.launch(googleSignInClient.signInIntent)
            }
        }

        containerProfileLayout.addView(emailInput)
        containerProfileLayout.addView(passwordInput)
        containerProfileLayout.addView(loginButton)
        containerProfileLayout.addView(registerButton)
        containerProfileLayout.addView(googleButton)
    }

    private fun mostrarBotonCerrarSesion() {
        val logout = Button(requireContext()).apply {
            text = "Cerrar sesión"
            setTextColor(resources.getColor(android.R.color.holo_red_dark, null))
            setOnClickListener {
                auth.signOut()
                Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()
                actualizarVista()
            }
        }
        containerProfileLayout.addView(logout)
    }

    private fun mostrarBotonesBackup() {
        val routineVM = ViewModelProvider(this)[RoutineViewModel::class.java]
        val sessionVM = ViewModelProvider(this)[TrainingHistoryViewModel::class.java]

        val backup = Button(requireContext()).apply {
            text = "Guardar Backup Manual"
            setOnClickListener {
                lifecycleScope.launch {
                    val rutinas = routineVM.getAllRoutines()
                    val sesiones = sessionVM.getAllSessions()
                    FirebaseRepository.saveBackupSnapshot(rutinas, sesiones,
                        onSuccess = {
                            Toast.makeText(requireContext(), "Backup guardado", Toast.LENGTH_SHORT).show()
                        },
                        onFailure = {
                            Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_LONG).show()
                        }
                    )
                }
            }
        }

        val restore = Button(requireContext()).apply {
            text = "Restaurar Backup"
            setOnClickListener {
                lifecycleScope.launch {
                    FirebaseRepository.restoreBackupSnapshot(
                        onResult = { routines, sessions ->
                            lifecycleScope.launch {
                                try {
                                    val db = DatabaseProvider.getDatabase(requireContext())
                                    db.routineDao().deleteAllRoutines()
                                    db.sessionDao().deleteAllSessions()
                                    routines.forEach { db.routineDao().insertRoutine(it) }
                                    sessions.forEach { db.sessionDao().insertSession(it) }

                                    Toast.makeText(requireContext(), "✅ Backup restaurado", Toast.LENGTH_SHORT).show()
                                    actualizarVista()
                                } catch (e: Exception) {
                                    Toast.makeText(requireContext(), "❌ Error al guardar en Room: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        onFailure = {
                            Toast.makeText(requireContext(), "❌ Error al restaurar backup: ${it.message}", Toast.LENGTH_LONG).show()
                        }
                    )
                }
            }
        }

        containerProfileLayout.addView(backup)
        containerProfileLayout.addView(restore)
    }

    private fun mostrarSeccionAcercaDe() {
        val separator = View(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2
            ).apply {
                setMargins(0, 24, 0, 24)
            }
            setBackgroundColor(resources.getColor(android.R.color.darker_gray, null))
        }

        val about = TextView(requireContext()).apply {
            text = "Acerca de\nFitTrackPlus v1.0\nDesarrollado por Álvaro Cervantes"
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(0, 24, 0, 24)
        }

        containerProfileLayout.addView(separator)
        containerProfileLayout.addView(about)
    }
}