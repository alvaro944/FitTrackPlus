package com.alvarocervantes.fittrackplus.ui.fragments

import android.content.Intent import android.os.Bundle import android.view.* import android.widget.* import androidx.activity.result.contract.ActivityResultContracts import androidx.core.content.ContextCompat import androidx.fragment.app.Fragment import androidx.lifecycle.ViewModelProvider import androidx.lifecycle.lifecycleScope import com.alvarocervantes.fittrackplus.R import com.alvarocervantes.fittrackplus.data.database.DatabaseProvider import com.alvarocervantes.fittrackplus.data.firebase.FirebaseRepository import com.alvarocervantes.fittrackplus.viewmodel.RoutineViewModel import com.alvarocervantes.fittrackplus.viewmodel.TrainingHistoryViewModel import com.bumptech.glide.Glide import com.google.android.material.button.MaterialButton import com.google.android.material.textfield.TextInputLayout import com.google.firebase.auth.FirebaseAuth import com.google.firebase.auth.GoogleAuthProvider import com.google.android.gms.auth.api.signin.GoogleSignIn import com.google.android.gms.auth.api.signin.GoogleSignInClient import com.google.android.gms.auth.api.signin.GoogleSignInOptions import com.google.android.gms.common.api.ApiException import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: androidx.activity.result.ActivityResultLauncher<Intent>
    private lateinit var containerProfileLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }

    private fun actualizarVista() {
        val layoutLoginForm = requireView().findViewById<LinearLayout>(R.id.layoutLoginForm)
        val layoutLoggedIn = requireView().findViewById<LinearLayout>(R.id.layoutLoggedIn)
        val backupSection = requireView().findViewById<LinearLayout>(R.id.layoutBackupSection)

        val emailEditText = requireView().findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = requireView().findViewById<EditText>(R.id.passwordEditText)
        val emailLayout = requireView().findViewById<TextInputLayout>(R.id.emailLayout)
        val passwordLayout = requireView().findViewById<TextInputLayout>(R.id.passwordLayout)
        val buttonLogin = requireView().findViewById<MaterialButton>(R.id.buttonLogin)
        val buttonRegister = requireView().findViewById<MaterialButton>(R.id.buttonRegister)
        val buttonGoogle = requireView().findViewById<MaterialButton>(R.id.buttonGoogleLogin)

        val imageAvatar = requireView().findViewById<ImageView>(R.id.imageUserAvatar)
        val textName = requireView().findViewById<TextView>(R.id.textUserName)
        val buttonLogout = requireView().findViewById<MaterialButton>(R.id.buttonLogout)

        val buttonBackup = requireView().findViewById<MaterialButton>(R.id.buttonBackup)
        val buttonRestore = requireView().findViewById<MaterialButton>(R.id.buttonRestore)

        val user = auth.currentUser

        if (user != null) {
            layoutLoginForm.visibility = View.GONE
            layoutLoggedIn.visibility = View.VISIBLE
            backupSection.visibility = View.VISIBLE

            textName.text = user.displayName ?: user.email ?: "Usuario"
            Glide.with(this)
                .load(user.photoUrl)
                .circleCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageAvatar)

            buttonLogout.setOnClickListener {
                auth.signOut()
                Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()
                actualizarVista()
            }

            val routineVM = ViewModelProvider(this)[RoutineViewModel::class.java]
            val sessionVM = ViewModelProvider(this)[TrainingHistoryViewModel::class.java]

            buttonBackup.setOnClickListener {
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

            buttonRestore.setOnClickListener {
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
        } else {
            layoutLoginForm.visibility = View.VISIBLE
            layoutLoggedIn.visibility = View.GONE
            backupSection.visibility = View.GONE

            buttonLogin.setOnClickListener {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                var isValid = true

                if (email.isBlank()) {
                    emailLayout.error = "El correo no puede estar vacío"
                    isValid = false
                } else {
                    emailLayout.error = null
                }

                if (password.length < 6) {
                    passwordLayout.error = "La contraseña debe tener al menos 6 caracteres"
                    isValid = false
                } else {
                    passwordLayout.error = null
                }

                if (isValid) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            actualizarVista()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "❌ Error: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            buttonRegister.setOnClickListener {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                var isValid = true

                if (email.isBlank()) {
                    emailLayout.error = "El correo no puede estar vacío"
                    isValid = false
                } else {
                    emailLayout.error = null
                }

                if (password.length < 6) {
                    passwordLayout.error = "La contraseña debe tener al menos 6 caracteres"
                    isValid = false
                } else {
                    passwordLayout.error = null
                }

                if (isValid) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            actualizarVista()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "❌ Error: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            buttonGoogle.setOnClickListener {
                googleSignInLauncher.launch(googleSignInClient.signInIntent)
            }
        }
    }
}