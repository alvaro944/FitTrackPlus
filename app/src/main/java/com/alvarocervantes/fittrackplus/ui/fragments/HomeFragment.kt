package com.alvarocervantes.fittrackplus.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.alvarocervantes.fittrackplus.R
import com.alvarocervantes.fittrackplus.data.preferences.LastRoutineManager
import com.alvarocervantes.fittrackplus.viewmodel.RegisterTrainingViewModel
import com.alvarocervantes.fittrackplus.viewmodel.RoutineViewModel
import com.alvarocervantes.fittrackplus.viewmodel.TrainingHistoryViewModel
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class HomeFragment : Fragment() {

    private val routineVM: RoutineViewModel by viewModels()
    private val sessionVM: TrainingHistoryViewModel by viewModels()
    private val registerVM: RegisterTrainingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botón para ver historial desde resumen
        val buttonViewHistory = view.findViewById<Button>(R.id.buttonVerHistorialDesdeResumen)
        buttonViewHistory.setOnClickListener {
            val lastRoutineId = LastRoutineManager.getLastRoutineId(requireContext())
            if (lastRoutineId != -1L) {
                val bundle = Bundle().apply {
                    putLong("routineId", lastRoutineId)
                }
                findNavController().navigate(R.id.trainingHistoryFragment, bundle)
            } else {
                Toast.makeText(requireContext(), "❗Selecciona una rutina primero", Toast.LENGTH_SHORT).show()
            }
        }

        val buttonVerRutinas = view.findViewById<Button>(R.id.buttonVerRutinas)
        buttonVerRutinas.setOnClickListener {
            findNavController().navigate(R.id.addTrainingFragment)
        }

        val buttonEntrenarAhora = view.findViewById<Button>(R.id.buttonEntrenarAhora)
        buttonEntrenarAhora.setOnClickListener {
            val routineId = LastRoutineManager.getLastRoutineId(requireContext())
            if (routineId != -1L) {
                val bundle = Bundle().apply {
                    putLong("routineId", routineId)
                }
                findNavController().navigate(R.id.registerTrainingFragment, bundle)
            } else {
                Toast.makeText(requireContext(), "❗Selecciona una rutina primero", Toast.LENGTH_SHORT).show()
            }
        }

        cargarFraseMotivacionalDesdeAssets(view)
    }

    override fun onResume() {
        super.onResume()

        view?.let { safeView ->
            val resumenText = safeView.findViewById<TextView>(R.id.textResumenRutina)
            val routineId = LastRoutineManager.getLastRoutineId(requireContext())

            if (routineId != -1L) {
                lifecycleScope.launch {
                    val routine = routineVM.getRoutineById(routineId)
                    val routineName = routine?.name ?: "Rutina activa"

                    val nextDay = registerVM.getNextRoutineDay(routineId)
                    val todayDay = nextDay?.dayName ?: "Día desconocido"

                    sessionVM.setRoutineId(routineId)
                    val sesiones = sessionVM.getTrainingHistory()
                    val ultimaSesion = sesiones.maxByOrNull { it.first.id }

                    val textoUltimaSesion = if (ultimaSesion != null) {
                        val dayName = ultimaSesion.first.dayName
                        val fechaFormateada = ultimaSesion.first.date?.let { dateIso ->
                            try {
                                val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                val parsedDate = parser.parse(dateIso)
                                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                formatter.format(parsedDate!!)
                            } catch (e: Exception) {
                                "Fecha inválida"
                            }
                        } ?: "Fecha no disponible"

                        "$dayName – $fechaFormateada"
                    } else {
                        "Aún no has registrado sesiones"
                    }

                    resumenText.text = """
                        🏋️ Rutina activa: $routineName
                        📆 Hoy toca: $todayDay
                        🕓 Última sesión: $textoUltimaSesion
                    """.trimIndent()
                }
            } else {
                resumenText.text = "No hay rutina activa seleccionada"
            }
        }
    }

    private fun cargarFraseMotivacionalDesdeAssets(view: View) {
        try {
            val inputStream = requireContext().assets.open("frases_gym.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val frasesArray = JSONArray(jsonString)

            if (frasesArray.length() > 0) {
                val randomIndex = Random.nextInt(frasesArray.length())
                val fraseObj = frasesArray.getJSONObject(randomIndex)
                val frase = fraseObj.getString("frase")
                val autor = fraseObj.optString("autor", "Anónimo")

                view.findViewById<TextView>(R.id.textMotivation).text = "“$frase”"
                view.findViewById<TextView>(R.id.textAuthor).text = "- $autor"
            } else {
                view.findViewById<TextView>(R.id.textMotivation).text = "No hay frases disponibles."
                view.findViewById<TextView>(R.id.textAuthor).text = ""
            }
        } catch (e: Exception) {
            view.findViewById<TextView>(R.id.textMotivation).text = "No se pudo cargar frase"
            view.findViewById<TextView>(R.id.textAuthor).text = ""
            e.printStackTrace()
        }
    }
}