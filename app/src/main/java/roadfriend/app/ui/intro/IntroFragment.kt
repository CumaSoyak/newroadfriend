package roadfriend.app.ui.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import roadfriend.app.R


class IntroFragment : Fragment() {

    private var drawable: Int = 0
    private var title: String? = null
    private var description: String? = null

    companion object {
        val TAG: String = IntroFragment::class.java.simpleName
        private const val ARG_TITLE = "title"
        private const val ARG_DESC = "desc"
        private const val ARG_DRAWABLE = "drawable"

        fun newInstance(title: String, description: String, imageDrawable: Int): IntroFragment {
            val sampleSlide = IntroFragment()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putString(ARG_DESC, description)
            args.putInt(ARG_DRAWABLE, imageDrawable)
            sampleSlide.arguments = args
            return sampleSlide
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null && arguments!!.size() != 0) {
            drawable = arguments!!.getInt(ARG_DRAWABLE)
            title = arguments!!.getString(ARG_TITLE)
            description = arguments!!.getString(ARG_DESC)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
