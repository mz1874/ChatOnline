package cn.org.bugcreator.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import cn.hutool.json.JSONUtil
import cn.org.bugcreator.R
import cn.org.bugcreator.activity.PersonalInformationActivity
import cn.org.bugcreator.vo.UserEntity
import com.bumptech.glide.Glide

class FriendListAdapter(private var list: List<UserEntity>) : RecyclerView.Adapter<FriendListAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellRow = layoutInflater.inflate(R.layout.friend_item_layout, parent, false)
        return CustomViewHolder(cellRow)
    }

    fun updateData(newItems: List<UserEntity>) {
        list = newItems
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        Log.i("SSS", JSONUtil.toJsonStr(list[position].name))
        holder.txtname.text = list[position].name
        Glide.with(holder.itemView.context)
            .load(list[position].imageSrc) // 图像的URL
//            .placeholder(R.drawable.placeholder_image) // 占位图像，可选
//            .error(R.drawable.error_image) // 加载失败时显示的图像，可选
            .into(holder.imageView) // 显示图像的ImageView

    }


    override fun getItemCount(): Int {
        return list.size
    }

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtname = view.findViewById<TextView>(R.id.txtname)
        val imageView = view.findViewById<ImageView>(R.id.img_view)
        init {
            // 设置item的点击事件监听器
            itemView.setOnClickListener {
                val position: Int = adapterPosition
                var currentData = list[position]
                val intent = Intent(view.context, PersonalInformationActivity::class.java)
                intent.putExtra("name",currentData.name)
                intent.putExtra("imageSrc",currentData.imageSrc)
                intent.putExtra("address",currentData.address)
                intent.putExtra("gender",currentData.isGender)

                startActivity(view.context, intent, null)
            }
        }
    }


}
