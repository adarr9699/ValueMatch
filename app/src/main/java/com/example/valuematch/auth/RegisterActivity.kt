import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.valuematch.R
import com.example.valuematch.profile.EditProfileActivity
import com.example.valuematch.utils.FirebaseUtils

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val email = findViewById<EditText>(R.id.emailInput)
        val password = findViewById<EditText>(R.id.passwordInput)
        val registerBtn = findViewById<Button>(R.id.registerBtn)

        registerBtn.setOnClickListener {
            FirebaseUtils.auth.createUserWithEmailAndPassword(
                email.text.toString(),
                password.text.toString()
            ).addOnSuccessListener {
                startActivity(Intent(this, EditProfileActivity::class.java))
            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}