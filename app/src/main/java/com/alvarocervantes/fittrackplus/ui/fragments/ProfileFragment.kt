package com.alvarocervantes.fittrackplus.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.alvarocervantes.fittrackplus.R
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient


class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    private lateinit var containerProfileLayout: LinearLayout
    private var emailTemp: String = ""
    private var passwordTemp: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            val data = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(requireActivity()) { firebaseTask ->
                        if (firebaseTask.isSuccessful) {
                            Toast.makeText(requireContext(), "✅ Sesión iniciada con Google", Toast.LENGTH_SHORT).show()
                            actualizarVista()
                        } else {
                            Toast.makeText(requireContext(), "❌ Falló autenticación con Firebase", Toast.LENGTH_SHORT).show()
                        }
                    }
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), "❌ Falló inicio con Google", Toast.LENGTH_SHORT).show()
            }
        }

        actualizarVista()

        return view
    }

    private fun actualizarVista() {
        containerProfileLayout.removeAllViews()

        val currentUser = auth.currentUser

        if (currentUser == null) {
            mostrarFormularioLogin()
        } else {
            mostrarVistaUsuario(currentUser.email ?: "Usuario")
        }
    }

    private fun mostrarFormularioLogin() {
        val emailInput = EditText(requireContext()).apply {
            hint = "Email"
            inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
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
                } else {
                    Toast.makeText(requireContext(), "Introduce un email válido y una contraseña de al menos 6 caracteres", Toast.LENGTH_SHORT).show()
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
                } else {
                    Toast.makeText(requireContext(), "Introduce un email válido y una contraseña de al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val googleButton = Button(requireContext()).apply {
            text = "Iniciar sesión con Google"
            setOnClickListener {
                val signInIntent = googleSignInClient.signInIntent
                googleSignInLauncher.launch(signInIntent)
            }
        }

        // Añadir todos los elementos al layout principal
        containerProfileLayout.addView(emailInput)
        containerProfileLayout.addView(passwordInput)
        containerProfileLayout.addView(loginButton)
        containerProfileLayout.addView(registerButton)
        containerProfileLayout.addView(googleButton)
    }


    private fun mostrarVistaUsuario(email: String) {
        val textUser = TextView(requireContext()).apply {
            text = "Sesión iniciada como: $email"
            textSize = 16f
            setPadding(0, 0, 0, 24)
        }

        val logoutButton = Button(requireContext()).apply {
            text = "Cerrar sesión"
            setOnClickListener {
                auth.signOut()
                Toast.makeText(requireContext(), "🔒 Sesión cerrada", Toast.LENGTH_SHORT).show()
                actualizarVista()
            }
        }

        containerProfileLayout.addView(textUser)
        containerProfileLayout.addView(logoutButton)
    }
}
