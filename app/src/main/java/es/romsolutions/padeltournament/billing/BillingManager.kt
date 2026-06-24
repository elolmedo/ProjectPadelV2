package es.romsolutions.padeltournament.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BillingManager(private val context: Context) : PurchasesUpdatedListener {
    private val TAG = "BillingManager"
    private val PRO_VERSION_ID = "pro_version_lifetime"

    private val _isPro = MutableStateFlow(false)
    val isPro = _isPro.asStateFlow()

    private val billingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases()
        .build()

    init {
        startConnection()
    }

    private fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Conexión con Play Store exitosa")
                    queryPurchases()
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.w(TAG, "Conexión con Play Store perdida. Reintentando...")
                startConnection()
            }
        })
    }

    fun queryPurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        billingClient.queryPurchasesAsync(params) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val hasPro = purchases.any { purchase ->
                    purchase.products.contains(PRO_VERSION_ID) && purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                }
                _isPro.value = hasPro
                Log.d(TAG, "Estado PRO detectado: $hasPro")
                
                purchases.forEach { purchase ->
                    if (!purchase.isAcknowledged && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        acknowledgePurchase(purchase)
                    }
                }
            }
        }
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        billingClient.acknowledgePurchase(params) { billingResult ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.d(TAG, "Compra confirmada correctamente")
            }
        }
    }

    fun launchBillingFlow(activity: Activity) {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(PRO_VERSION_ID)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                val productDetails = productDetailsList[0]
                val flowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(listOf(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(productDetails)
                            .build()
                    ))
                    .build()
                billingClient.launchBillingFlow(activity, flowParams)
            } else {
                Log.e(TAG, "Error al obtener detalles del producto: ${billingResult.debugMessage}")
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            queryPurchases()
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.d(TAG, "Usuario canceló la compra")
        } else {
            Log.e(TAG, "Error en la compra: ${billingResult.debugMessage}")
        }
    }
}