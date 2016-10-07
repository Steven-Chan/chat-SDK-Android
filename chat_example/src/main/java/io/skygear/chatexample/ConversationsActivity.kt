package io.skygear.chatexample

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import io.skygear.plugins.chat.ChatContainer
import io.skygear.plugins.chat.Conversation
import io.skygear.skygear.Container
import io.skygear.skygear.LogoutResponseHandler

class ConversationsActivity : AppCompatActivity() {
    private val LOG_TAG: String? = "ConversationsActivity"

    private var mSkygear: Container? = null
    private var mChatMgr: ChatContainer? = null
    private val mAdapter: ConversationsAdapter = ConversationsAdapter()
    private var mConversationsRv: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversations)

        mSkygear = Container.defaultContainer(this)
        mChatMgr = ChatContainer.getInstance(mSkygear)

        mConversationsRv = findViewById(R.id.conversations_rv) as RecyclerView
        mConversationsRv?.adapter = mAdapter
        mConversationsRv?.layoutManager = LinearLayoutManager(this)
        mAdapter.setOnClickListener {
            c -> showOptions(c)
        }
    }

    override fun onResume() {
        super.onResume()

        mChatMgr?.getConversations { list, s ->
            val conversations = list as List<Conversation>
            mAdapter.setConversations(conversations)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.conversation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.log_out_menu -> {
                confirmLogOut()
                return true
            }
            R.id.add_conversation_menu -> {
                startActivity(Intent(this, CreateConversationActivity::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun confirmLogOut() {
        AlertDialog.Builder(this)
                .setTitle(R.string.confirm)
                .setMessage(R.string.are_your_sure_to_log_out)
                .setPositiveButton(R.string.yes) { dialog, which -> logOut() }
                .setNegativeButton(R.string.no, null).show()
    }

    fun logOut() {
        val loading = ProgressDialog(this)
        loading.setTitle(R.string.loading)
        loading.setMessage(getString(R.string.logging_out))
        loading.show()

        mSkygear?.logout(object : LogoutResponseHandler() {
            override fun onLogoutSuccess() {
                loading.dismiss()

                logoutSuccess()
            }

            override fun onLogoutFail(reason: String) {
                loading.dismiss()

                logoutFail()
            }
        })
    }

    fun logoutSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun logoutFail() {
        AlertDialog.Builder(this).setTitle(R.string.logout_failed).show()
    }

    fun showOptions(c: Conversation) {
        val builder = AlertDialog.Builder(this)
        val items = arrayOf<CharSequence>(getString(R.string.enter),
                getString(R.string.edit), getString(R.string.delete))
        builder.setItems(items, object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
        builder.setItems(items, { d, i -> when(i) {
            0 -> enter(c)
        } })
        val alert = builder.create()
        alert.show()
    }

    fun enter(c: Conversation) {
        startActivity(ConversationActivity.newIntent(c, this))
    }
}