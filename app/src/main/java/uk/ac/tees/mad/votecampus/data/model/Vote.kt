package uk.ac.tees.mad.votecampus.data.model

data class Vote(
    val userId: String = "",
    val pollId: String = "",
    val selectedOption: String = "",
    val timestamp: Long = 0L
)

data class VoteCount(
    val option: String = "",
    val count: Int = 0,
    val percentage: Float = 0f
)