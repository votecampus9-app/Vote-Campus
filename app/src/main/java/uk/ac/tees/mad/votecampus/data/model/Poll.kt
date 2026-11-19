package uk.ac.tees.mad.votecampus.data.model

data class Poll(
    val id: String = "",
    val title: String = "",
    val options: List<String> = emptyList(),
    val active: Boolean = true,
    val createdAt: Long = 0L
)