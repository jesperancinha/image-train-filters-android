package org.jesperancinha.itf.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class ViewFragment : Fragment() {
    private var mainView: View? = null
    var imageView: ImageView? = null
        private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.content_view, container, false)
            imageView = requireNotNull(mainView).findViewById(R.id.imageViewGenerated)
        }
        return mainView
    }
}