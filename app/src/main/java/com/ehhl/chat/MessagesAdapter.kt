package com.ehhl.chat

import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_mensagem.view.*
import android.widget.FrameLayout
import android.view.animation.AnimationUtils


class MessagesAdapter(private val msgList: List<Mensagem>, val nick: String) :
        RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {

    lateinit var context: Context
    var lastPosition = -1;

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mTxtMensagem = mView.txtLstMensagem
        val mTxtRemetente = mView.txtRemetente
        val mBolha = mView.bolha



        override fun toString(): String {
            return super.toString() + " '" + mTxtMensagem.text + "'"
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_mensagem, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    // Replace the contents of a view
    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.mTxtMensagem.text = msgList[i].dados
        holder.mTxtRemetente.text = msgList[i].from
        if(msgList[i].from.equals(nick)){
            holder.mBolha.setBackgroundResource(R.drawable.bolha_mensagem)
            val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT)
            params.gravity = Gravity.RIGHT
            params.rightMargin = 0
            params.leftMargin = 64
            holder.mBolha.layoutParams = params
            holder.mTxtMensagem.gravity = Gravity.RIGHT
        }else{
            holder.mBolha.setBackgroundResource(R.drawable.bolha_mensagem2)
            val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT)
            params.gravity = Gravity.LEFT
            params.rightMargin = 64
            params.leftMargin = 0
            holder.mBolha.layoutParams = params
            holder.mTxtMensagem.gravity = Gravity.LEFT
        }

        setAnimation(holder.itemView, i);

    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_down)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    override fun getItemCount() = msgList.size
}