package es.romsolutions.padeltournament.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class AnalyticsManager(context: Context) {
    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    fun logScreenView(screenName: String) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenName)
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    fun logTournamentCreated(type: String, numPlayers: Int, isTeamBased: Boolean) {
        val bundle = Bundle().apply {
            putString("tournament_type", type)
            putInt("num_players", numPlayers)
            putBoolean("is_team_based", isTeamBased)
        }
        firebaseAnalytics.logEvent("tournament_created", bundle)
    }

    fun logLeagueCreated(numTeams: Int) {
        val bundle = Bundle().apply {
            putInt("num_teams", numTeams)
        }
        firebaseAnalytics.logEvent("league_created", bundle)
    }

    fun logPlayerCreated(method: String) {
        val bundle = Bundle().apply {
            putString("creation_method", method) // "manual" or "contacts"
        }
        firebaseAnalytics.logEvent("player_created", bundle)
    }

    fun logUpgradeProClick() {
        firebaseAnalytics.logEvent("click_upgrade_pro", null)
    }

    fun logPurchaseComplete(orderId: String) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.TRANSACTION_ID, orderId)
            putDouble(FirebaseAnalytics.Param.VALUE, 3.99)
            putString(FirebaseAnalytics.Param.CURRENCY, "EUR")
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.PURCHASE, bundle)
    }

    fun setUserPro(isPro: Boolean) {
        firebaseAnalytics.setUserProperty("user_type", if (isPro) "PRO" else "FREE")
    }

    fun logRoundRotated(tournamentId: Int, roundNumber: Int) {
        val bundle = Bundle().apply {
            putInt("tournament_id", tournamentId)
            putInt("round_number", roundNumber)
        }
        firebaseAnalytics.logEvent("round_rotated", bundle)
    }
}
