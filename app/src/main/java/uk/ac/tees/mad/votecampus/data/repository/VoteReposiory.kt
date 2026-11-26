package uk.ac.tees.mad.votecampus.data.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.votecampus.data.model.Poll
import uk.ac.tees.mad.votecampus.data.model.Vote
import uk.ac.tees.mad.votecampus.data.model.VoteCount

class VoteRepository {

    private val db = Firebase.firestore

    fun getActivePolls(): Flow<List<Poll>> = callbackFlow {
        val listener = db.collection("polls")
            .whereEqualTo("active", true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val polls = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Poll::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                trySend(polls)
            }
        awaitClose { listener.remove() }
    }

    fun getPollResults(pollId: String): Flow<List<VoteCount>> = callbackFlow {
        val listener = db.collection("votes")
            .whereEqualTo("pollId", pollId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val votes = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Vote::class.java)
                } ?: emptyList()
                val total = votes.size.toFloat().coerceAtLeast(1f)
                val results = votes.groupBy { it.selectedOption }
                    .map { (option, list) ->
                        VoteCount(
                            option = option,
                            count = list.size,
                            percentage = (list.size / total) * 100f
                        )
                    }
                    .sortedByDescending { it.count }
                trySend(results)
            }
        awaitClose { listener.remove() }
    }

    suspend fun submitVote(vote: Vote): Result<Unit> {
        return try {
            val docId = "${vote.userId}_${vote.pollId}"
            db.collection("votes").document(docId).set(vote).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun hasUserVoted(userId: String, pollId: String): Boolean {
        return try {
            val docId = "${userId}_${pollId}"
            val doc = db.collection("votes").document(docId).get().await()
            doc.exists()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getPoll(pollId: String): Poll? {
        return try {
            val doc = db.collection("polls").document(pollId).get().await()
            doc.toObject(Poll::class.java)?.copy(id = doc.id)
        } catch (e: Exception) {
            null
        }
    }
}