package es.romsolutions.padeltournament.ui.components

import android.app.Activity
import android.content.Intent
import android.provider.ContactsContract
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
import es.romsolutions.padeltournament.data.model.Player

@Composable
fun AddPlayerDialog(onDismiss: () -> Unit, onSave: (Player) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var nombreError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    
    val context = LocalContext.current
    val contactLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                context.contentResolver.query(uri, arrayOf(ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME), null, null, null)?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                        nombre = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                        context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?", arrayOf(contactId), null)?.use { pc ->
                            if (pc.moveToFirst()) phone = pc.getString(pc.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)).filter { it.isDigit() }
                        }
                        context.contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ?", arrayOf(contactId), null)?.use { ec ->
                            if (ec.moveToFirst()) email = ec.getString(ec.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.ADDRESS))
                        }
                    }
                }
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Nuevo Jugador", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                Button(
                    onClick = { contactLauncher.launch(Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)) }, 
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
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }
                    Button(onClick = {
                        var ok = true
                        if(nombre.length<2) { nombreError="Mínimo 2 letras"; ok=false }
                        if(phone.length<9) { phoneError="Mínimo 9 dígitos"; ok=false }
                        if(ok) onSave(Player(nombre = nombre, phone = phone, email = email))
                    }) { Text("Guardar") }
                }
            }
        }
    }
}
