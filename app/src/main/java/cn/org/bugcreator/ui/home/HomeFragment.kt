package cn.org.bugcreator.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.hutool.json.JSON
import cn.hutool.json.JSONUtil
import cn.org.bugcreator.R
import cn.org.bugcreator.adapter.FriendListAdapter
import cn.org.bugcreator.databinding.FragmentHomeBinding
import cn.org.bugcreator.util.OkHttpTool
import cn.org.bugcreator.vo.CommonResponse
import cn.org.bugcreator.vo.UserEntity
import cn.org.bugcreator.vo.UserVo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.text.observe(viewLifecycleOwner) {
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        CoroutineScope(Dispatchers.IO).launch {
            val result = OkHttpTool.get("http://192.168.0.4:8081/userFriend/selectUserFriendsByUserId")
            val responseType = object : TypeToken<CommonResponse<List<UserEntity>>>() {}.type
            val resObj: CommonResponse<List<UserEntity>> = Gson().fromJson(result, responseType)
            Log.i("SSS",result )
            withContext(Dispatchers.Main) {
                recyclerView.adapter = FriendListAdapter(resObj.data);
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}