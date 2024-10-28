package org.jesperancinha.itf.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment

class EmailFragment : Fragment() {
    private var mainView: View? = null
    var textTo: EditText? = null
        private set
    var textCC: EditText? = null
        private set
    var textBCC: EditText? = null
        private set
    var textEmail: EditText? = null
        private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.content_email, container, false)
            textTo = requireNotNull(mainView).findViewById(R.id.editEmailTo)
            textCC = requireNotNull(mainView).findViewById(R.id.editEmailCC)
            textBCC = requireNotNull(mainView).findViewById(R.id.editEmailBCC)
            textEmail = requireNotNull(mainView).findViewById(R.id.editEmailText)
        }
        return mainView
    }
}