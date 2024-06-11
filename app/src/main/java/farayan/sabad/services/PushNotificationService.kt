package farayan.sabad.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class PushNotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("PushNotificationService", "Refreshed token: $token")
    }
}