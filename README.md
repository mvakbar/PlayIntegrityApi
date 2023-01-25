# Play-Integrity-API-Kotlin
Play Integrity API android kotlin

If you decide to use the Play Integrity API, you should do the following. 

1. First of all you should add implementation of Play-Integrity-API in buil.gradle 

        implementation com.google.android.play:integrity:1.0.2

2. Befor getting  integrity token , we need  nonce . For generating  nonce we get unique value form  app's server-side backend  

        private fun generateNonce(): String? {
         val length = 50
         var nonce = ""
         
         val allowed = "ABCDEFGHIJKLMNOPQRSTUVWXYZertqwrtabcdefghijklmnopqrstuvwxyz012sdsad3456789"
         for (i in 0 until length) {
             nonce = nonce + allowed[Math.floor(Math.random() * allowed.length).toInt()].toString()
         }
         return nonce
        }

3. You should use IntegrityManagerFactory for getting integrity token .
    
    
       val nonce: String =generateNonce()

        // Create an instance of a manager.
        val integrityManager = IntegrityManagerFactory.create(applicationContext)

        // Request the integrity token by providing a nonce.
        val integrityTokenResponse = integrityManager.requestIntegrityToken(
            IntegrityTokenRequest.builder()
                .setNonce(nonce)
                .build()
        )
        integrityTokenResponse.addOnSuccessListener { integrityTokenResponse: IntegrityTokenResponse ->
           val integrityToken = integrityTokenResponse.token()
        <!--      If there is not any problem , and recuest is succefull        -->
        <!--      After getting integrityToken , you send it to app's server-side backend than  backend  return you decision   -->
        }
        integrityTokenResponse.addOnFailureListener { e: Exception? ->
         <!--      If there is any problem -->     
        }
        
    

        
![api-usage](https://user-images.githubusercontent.com/48914921/213987860-a35eccba-808b-43bd-a322-5c14712fc52a.svg)


When the user performs a high-value action in your app that you want to protect with an integrity check, complete the following steps:

  1.Your app's server-side backend generates and sends a unique value to the client-side logic. The remaining steps refer to this logic as your “app.”

  2.Your app creates the nonce from the unique value and the content of your high-value action. It then calls the Play Integrity API, passing in the nonce.

  3.Your app receives a signed and encrypted verdict from the Play Integrity API.

  4.Your app passes the signed and encrypted verdict to your app's backend.
  
  5.Your app's backend sends the verdict to a Google Play server. The Google Play server decrypts and verifies the verdict, returning the results to your app's   backend.
  
  6.Your app's backend decides how to proceed, based on the signals contained in the token payload.
  
  7.Your app's backend sends the decision outcomes to your app.



       
        
        
