package com.alvarocervantes.fittrackplus.data.firebase

import com.alvarocervantes.fittrackplus.data.model.RoutineEntity
import com.alvarocervantes.fittrackplus.data.model.SessionEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseRepository {

    private val db = FirebaseFirestore.getInstance()
    private val user get() = FirebaseAuth.getInstance().currentUser

    fun uploadRoutine(routine: RoutineEntity) {
        user?.let {
            db.collection("users")
                .document(it.uid)
                .collection("routines")
                .document(routine.id.toString())
                .set(routine)
        }
    }

    fun uploadSession(session: SessionEntity) {
        user?.let {
            db.collection("users")
                .document(it.uid)
                .collection("sessions")
                .document(session.id.toString())
                .set(session)
        }
    }

    suspend fun saveBackupSnapshot(
        routines: List<RoutineEntity>,
        sessions: List<SessionEntity>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val user = FirebaseAuth.getInstance().currentUser ?: return

        val backupRef = db.collection("backups")
            .document(user.uid)

        val routinesMap = routines.associateBy { it.id.toString() }
        val sessionsMap = sessions.associateBy { it.id.toString() }

        val data = mapOf(
            "snapshot_routines" to routinesMap,
            "snapshot_sessions" to sessionsMap,
            "timestamp" to System.currentTimeMillis()
        )

        backupRef.set(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
    suspend fun restoreBackupSnapshot(
        onResult: (List<RoutineEntity>, List<SessionEntity>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val user = FirebaseAuth.getInstance().currentUser ?: return

        db.collection("backups")
            .document(user.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    try {
                        val routinesMap = document.get("snapshot_routines") as? Map<String, Map<String, Any>>
                        val sessionsMap = document.get("snapshot_sessions") as? Map<String, Map<String, Any>>

                        val routines = routinesMap?.values?.mapNotNull { map ->
                            mapToRoutineEntity(map)
                        } ?: emptyList()

                        val sessions = sessionsMap?.values?.mapNotNull { map ->
                            mapToSessionEntity(map)
                        } ?: emptyList()

                        onResult(routines, sessions)

                    } catch (e: Exception) {
                        onFailure(e)
                    }
                } else {
                    onResult(emptyList(), emptyList())
                }
            }
            .addOnFailureListener {
                onFailure(it)
            }
    }
    private fun mapToRoutineEntity(map: Map<String, Any>): RoutineEntity? {
        return try {
            RoutineEntity(
                id = (map["id"] as Number).toLong(),
                name = map["name"] as String
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun mapToSessionEntity(map: Map<String, Any>): SessionEntity? {
        return try {
            SessionEntity(
                id = (map["id"] as Number).toLong(),
                routineId = (map["routineId"] as Number).toLong(),
                dayId = (map["dayId"] as Number).toLong(),
                date = map["date"] as String,
                week = (map["week"] as Number).toInt(),
                dayName = map["dayName"] as String,
                comment = map["comment"] as? String
            )
        } catch (e: Exception) {
            null
        }
    }

}
