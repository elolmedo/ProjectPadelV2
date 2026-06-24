package es.romsolutions.padeltournament.ui.components

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import es.romsolutions.padeltournament.data.model.Player

@Composable
fun AddPlayerDialog(
    authManager: es.romsolutions.padeltournament.auth.AuthManager? = null,
    onDismiss: () -> Unit,
    onSave: (Player) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<String?>(null) }
    var level by remember { mutableStateOf(3.0) }
    var nombreError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    
    val context = LocalContext.current
    
    val contactLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                try {
                    context.contentResolver.query(
                        uri, 
                        arrayOf(
                            ContactsContract.Contacts._ID, 
                            ContactsContract.Contacts.DISPLAY_NAME,
                            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
                        ), 
                        null, null, null
                    )?.use { cursor ->
                        if (cursor.moveToFirst()) {
                            val idIdx = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                            val nameIdx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                            val photoIdx = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)
                            
                            if (idIdx != -1 && nameIdx != -1) {
                                val contactId = cursor.getString(idIdx)
                                nombre = cursor.getString(nameIdx)
                                if (photoIdx != -1) {
                                    photoUri = cursor.getString(photoIdx)
                                }
                                
                                context.contentResolver.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
                                    null, 
                                    "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?", 
                                    arrayOf(contactId), 
                                    null
                                )?.use { pc ->
                                    if (pc.moveToFirst()) {
                                        val phoneIdx = pc.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                        if (phoneIdx != -1) {
                                            phone = pc.getString(phoneIdx).filter { it.isDigit() || it == '+' }
                                        }
                                    }
                                }
                                
                                context.contentResolver.query(
                                    ContactsContract.CommonDataKinds.Email.CONTENT_URI, 
                                    null, 
                                    "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ?", 
                                    arrayOf(contactId), 
                                    null
                                )?.use { ec ->
                                    if (ec.moveToFirst()) {
                                        val emailIdx = ec.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
                                        if (emailIdx != -1) {
                                            email = ec.getString(emailIdx)
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error al importar contacto", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            contactLauncher.launch(Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI))
        } else {
            Toast.makeText(context, "Permiso denegado para leer contactos", Toast.LENGTH_SHORT).show()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Nuevo Jugador", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                Button(
                    onClick = { 
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) -> {
                                contactLauncher.launch(Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI))
                            }
                            else -> {
                                permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) { Text("Importar de Agenda") }
                
                OutlinedTextField(
                    value = nombre, 
                    onValueChange = { nombre = it; if(it.length>=2) nombreError=null }, 
                    label = { Text("Nombre") }, 
                    isError = nombreError != null, 
                    supportingText = { nombreError?.let { Text(it) } }, 
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone, 
                    onValueChange = { if(it.all { c -> c.isDigit() }) { phone = it; if(it.length>=9) phoneError=null } }, 
                    label = { Text("Teléfono") }, 
                    isError = phoneError != null, 
                    supportingText = { phoneError?.let { Text(it) } }, 
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email, 
                    onValueChange = { email = it }, 
                    label = { Text("Email") }, 
                    modifier = Modifier.fillMaxWidth()
                )
                
                Text(text = "Nivel: ${String.format("%.1f", level)}", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 8.dp))
                Slider(
                    value = level.toFloat(),
                    onValueChange = { level = it.toDouble() },
                    valueRange = 1.0f..7.0f,
                    steps = 11 // incrementos de 0.5 (1.0, 1.5, 2.0...)
                )
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }
                    Button(onClick = {
                        var ok = true
                        if(nombre.length<2) { nombreError="Mínimo 2 letras"; ok=false }
                        if(phone.length<9) { phoneError="Mínimo 9 dígitos"; ok=false }
                        if(ok) {
                            onSave(Player(
                                nombre = nombre, 
                                phone = phone, 
                                email = email, 
                                level = level,
                                adminId = authManager?.getCurrentUserId(),
                                photoUri = photoUri
                            ))
                        }
                    }) { Text("Guardar") }
                }
            }
        }
    }
}
