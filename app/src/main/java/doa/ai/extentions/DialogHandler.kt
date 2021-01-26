package doa.ai.extentions

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.method.ScrollingMovementMethod
import doa.ai.R
import kotlinx.android.synthetic.main.dialog_info.*

object Dialog{

    private lateinit var dialog: Dialog

    fun progressDialog(context: Context?, text: String){
        dialog = Dialog(context as Context)
        dialog.setContentView(R.layout.dialog_info)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.textInfoDialog.text = text
        dialog.textInfoDialog.movementMethod = ScrollingMovementMethod()
        dialog.imageInfoClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    fun dismissDialog(){
        dialog.dismiss()
    }
}
