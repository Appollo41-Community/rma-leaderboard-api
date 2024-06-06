package rs.raf.edu.rma.leaderboard.utils

fun isNicknameValid(username: String?): Boolean {
    return username?.all { it.isLetterOrDigit() || it == '_' } == true
}
