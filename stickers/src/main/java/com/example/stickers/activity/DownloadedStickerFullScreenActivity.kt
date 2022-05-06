package com.example.stickers.activity

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.*
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.provider.Settings
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import cn.refactor.lib.colordialog.EnableDialog
import cn.refactor.lib.colordialog.SwitchDialog
import com.example.stickers.R
import com.example.stickers.adapter.DownloadedStickerFullScreenAdapter
import com.example.stickers.databinding.ActivityStickerFullScreenBinding
import com.example.stickers.fragment.DownloadedStickersFragment
import com.example.stickers.helper.FileHelper
import com.example.stickers.identities.StickerPacksContainer
import com.example.stickers.listener.StickerExpandedClickListener
import com.example.stickers.model.PreviewItem
import com.example.stickers.model.StickersListDownload
import com.example.stickers.preference.PreferenceManager
import com.example.stickers.utils.*
import com.example.stickers.view.TouchA
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.interfaces.DraweeController
import com.google.gson.Gson
import com.permissionx.guolindev.PermissionX
import com.squareup.picasso.Picasso
import java.io.File
import javax.annotation.Nonnull
import kotlin.math.max


class DownloadedStickerFullScreenActivity : AppCompatActivity(),StickerExpandedClickListener {

    companion object {
        const val STICKER_NAME = "stickerName"
        const val STICKER_ARRAYLIST = "stickerArrayList"
        const val STICKER_DELETE_PATH = "stickerDeletePath"
        const val STICKER_POSITION = "stickerPosition"
    }

    private lateinit var binding: ActivityStickerFullScreenBinding
    private lateinit var stickerName: String
    private lateinit var directoryPath: File
    private lateinit var stickerDeletePath: String
    private lateinit var arrayList: ArrayList<StickersListDownload>
    private val adapter = DownloadedStickerFullScreenAdapter(this,this)
    private var clickedStickerPreview: View? = null
    private val COLLAPSED_STICKER_PREVIEW_BACKGROUND_ALPHA = 1f
    private val EXPANDED_STICKER_PREVIEW_BACKGROUND_ALPHA = 0.2f
    private var expandedViewLeftX = 0f
    private var expandedViewTopY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStickerFullScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpUI()
        setUpAdapter()
        setAllClick()
        setBottomButtons()

    }

    private fun setAllClick() {
        with(binding) {
            textAddToWhatsApp.setOnSingleClickListener {
                PermissionX.init(this@DownloadedStickerFullScreenActivity)
                    .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .request { allGranted, _, deniedList ->
                        if (allGranted) {
                            addPackToWhatsApp()
                        } else {
                            Toast.makeText(
                                this@DownloadedStickerFullScreenActivity,
                                "These permissions are denied: $deniedList",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

            }
            viewWhatsapp.setOnSingleClickListener {
                PermissionX.init(this@DownloadedStickerFullScreenActivity)
                    .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .request { allGranted, _, deniedList ->
                        if (allGranted) {
                            addPackToWhatsApp()
                        } else {
                            Toast.makeText(
                                this@DownloadedStickerFullScreenActivity,
                                "These permissions are denied: $deniedList",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

            }
            textDownloadedShare.setOnSingleClickListener {
                PermissionX.init(this@DownloadedStickerFullScreenActivity)
                    .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .request { allGranted, _, deniedList ->
                        if (allGranted) {
                            shareSticker()
                        } else {
                            Toast.makeText(
                                this@DownloadedStickerFullScreenActivity,
                                "These permissions are denied: $deniedList",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

            }
            viewDownloadedShare.setOnSingleClickListener {
                PermissionX.init(this@DownloadedStickerFullScreenActivity)
                    .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .request { allGranted, _, deniedList ->
                        if (allGranted) {
                            shareSticker()

                        } else {
                            Toast.makeText(
                                this@DownloadedStickerFullScreenActivity,
                                "These permissions are denied: $deniedList",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
            viewDownloadedDelete.setOnSingleClickListener {
                PermissionX.init(this@DownloadedStickerFullScreenActivity)
                    .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .request { allGranted, _, deniedList ->
                        if (allGranted) {
                            deleteStickerPack()

                        } else {
                            Toast.makeText(
                                this@DownloadedStickerFullScreenActivity,
                                "These permissions are denied: $deniedList",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
            textDownloadedDelete.setOnSingleClickListener {
                PermissionX.init(this@DownloadedStickerFullScreenActivity)
                    .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .request { allGranted, _, deniedList ->
                        if (allGranted) {
                            deleteStickerPack()

                        } else {
                            Toast.makeText(
                                this@DownloadedStickerFullScreenActivity,
                                "These permissions are denied: $deniedList",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }
    }

    private fun setUpUI() {
        with(binding) {
            if (intent != null) {
               stickerName = intent.getStringExtra(STICKER_NAME).toString()
               arrayList = intent.getParcelableArrayListExtra<StickersListDownload>("stickerArrayList")!!
               stickerDeletePath = intent.getStringExtra(STICKER_DELETE_PATH).toString()
            }
            packName.text = stickerName
            mrlBack.setOnSingleClickListener {
                onBackPressed()
            }

            setupFirstImage()

        }
    }

    private fun setupFirstImage() {
        with(binding) {
            try {
                if (arrayList != null) {
                    Picasso.get()
                        .load(File(arrayList.get(0).getAbsolutePath()))
                        .placeholder(R.drawable.sticker_error).into(trayImage)
                }
            } catch (e: Exception) {
            }
        }
    }

    private fun setBottomButtons() {

        directoryPath = File(getStickerDownloadPath(this@DownloadedStickerFullScreenActivity) + "/")

        val existingPath = File(directoryPath.absolutePath + File.separator + stickerName)
        with(binding) {

            if (existingPath.exists()) {
                Log.w("msg", "existingPath: $existingPath")
                groupFreeEmoji.gone()
                groupWhatsApp.visible()
                groupShareSticker.gone()

            } else {
                Log.w("msg", "existingPath does not exist")
                groupFreeEmoji.visible()
                groupWhatsApp.gone()
                groupShareSticker.gone()
            }
            clDownloadedButtonView.visible()
        }
    }

    private fun setUpAdapter() {

        //set adapter in recyclerview
        binding.rvAllStickers.adapter = adapter

        adapter.setAllStickerList(arrayList)
    }

    override fun onClickExpand(
        position: Int,
        stickerItemPreview: View,
        allStickers: MutableList<PreviewItem>
    ) {
//        TODO("Not yet implemented")
    }

    override fun onDownloadedClickExpand(
        position: Int,
        stickerItemPreview: View,
        allStickers: MutableList<StickersListDownload>
    ) {
        this.clickedStickerPreview = stickerItemPreview

        with(binding) {
            if (isStickerPreviewExpanded()) {
                hideExpandedStickerPreview()
                return
            }
            if (!stickerDetailsExpandedSticker.equals(null)) {
                positionExpandedStickerPreview(position)
                Log.w("msg", "sticker full list---  " + allStickers[position].absolutePath)
                val controller: DraweeController = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(allStickers[position].absolutePath))
                    .setAutoPlayAnimations(true)
                    .build()
                with(stickerDetailsExpandedSticker) {
                    setImageResource(R.drawable.sticker_error)
                    setController(controller)
                    visible()
                }
                Picasso.get().load(File(allStickers[position].absolutePath))
                    .into(stickerDetailsExpandedSticker)

                rvAllStickers.alpha = EXPANDED_STICKER_PREVIEW_BACKGROUND_ALPHA
                stickerDetailsExpandedSticker.setOnSingleClickListener {
                    hideExpandedStickerPreview()
                }
            }
        }
    }

    private fun isStickerPreviewExpanded(): Boolean {
        return !binding.stickerDetailsExpandedSticker.equals(null) && binding.stickerDetailsExpandedSticker.visibility == View.VISIBLE
    }

    private fun hideExpandedStickerPreview() {
        with(binding) {
            if (isStickerPreviewExpanded() && !stickerDetailsExpandedSticker.equals(null)) {
                clickedStickerPreview?.visible()
                stickerDetailsExpandedSticker.visibility = View.INVISIBLE
                rvAllStickers.alpha = COLLAPSED_STICKER_PREVIEW_BACKGROUND_ALPHA
            }
        }
    }

    private fun positionExpandedStickerPreview(selectedPosition: Int) {
        with(binding) {
            // Calculate the view's center (x, y), then use expandedStickerPreview's height and
            // width to
            // figure out what where to position it.

            val recyclerViewLayoutParams =
                binding.rvAllStickers.layoutParams as ViewGroup.MarginLayoutParams
            val recyclerViewLeftMargin = recyclerViewLayoutParams.leftMargin
            val recyclerViewRightMargin = recyclerViewLayoutParams.rightMargin
            val recyclerViewWidth: Int = rvAllStickers.width
            val recyclerViewHeight: Int = rvAllStickers.height
            val clickedViewHolder: DownloadedStickerFullScreenAdapter.AllStickerViewHolder =
                rvAllStickers.findViewHolderForAdapterPosition(selectedPosition) as DownloadedStickerFullScreenAdapter.AllStickerViewHolder
            if (clickedViewHolder.equals(null)) {
                hideExpandedStickerPreview()
                return
            }
            clickedStickerPreview = clickedViewHolder.itemView
            val clickedViewCenterX =
                (clickedStickerPreview!!.x + recyclerViewLeftMargin + clickedStickerPreview!!.width / 2f)
            val clickedViewCenterY =
                clickedStickerPreview!!.y + clickedStickerPreview!!.height / 2f
            (clickedViewCenterX - stickerDetailsExpandedSticker.width / 2f).also { expandedViewLeftX = it }
            (clickedViewCenterY - stickerDetailsExpandedSticker.height / 2f).also { expandedViewTopY = it }

            // If the new x or y positions are negative, anchor them to 0 to avoid clipping
            // the left side of the device and the top of the recycler view.
            expandedViewLeftX = max(expandedViewLeftX, 0f)
            expandedViewTopY = max(expandedViewTopY, 0f)

            // If the bottom or right sides are clipped, we need to move the top left positions
            // so that those sides are no longer clipped.
            val adjustmentX = max(
                (expandedViewLeftX
                        + stickerDetailsExpandedSticker.width) - recyclerViewWidth
                        - recyclerViewRightMargin, 0f
            )
            val adjustmentY = max(
                expandedViewTopY + stickerDetailsExpandedSticker.height - recyclerViewHeight,
                0f
            )
            expandedViewLeftX -= adjustmentX
            expandedViewTopY -= adjustmentY
            stickerDetailsExpandedSticker.x = expandedViewLeftX
            stickerDetailsExpandedSticker.y = expandedViewTopY
        }
    }

    private fun addPackToWhatsApp() {

        if (StickerPacksManager.stickerPacksContainer == null) {
            val str = ""
            StickerPacksManager.stickerPacksContainer = StickerPacksContainer(
                str,
                str,
                StickerPacksManager.getStickerPacks(this@DownloadedStickerFullScreenActivity)
            )
        }
        WAFileUtils.initializeDirectories(this@DownloadedStickerFullScreenActivity)

        if (isOnline()) {
            val loadFiles: ArrayList<String> =
                FileHelper.loadFiles(directoryPath.absolutePath + File.separator + stickerName + File.separator)
            Log.w("msg", "whatsapp loadFiles 1 ---- " + directoryPath.absolutePath + File.separator + stickerName + File.separator)
            Log.w("msg", "whatsapp loadFiles size ---- " + loadFiles.size)

            if (loadFiles.size < 3) {
                Toast.makeText(this, getString(R.string.must_at_least_stickers_add_WhatsApp), Toast.LENGTH_LONG).show()
            } else if (packageManager.getLaunchIntentForPackage("com.whatsapp") == null && packageManager.getLaunchIntentForPackage("com.whatsapp.w4b") == null) {
                Toast.makeText(this, getString(R.string.whatsapp_not_installed), Toast.LENGTH_LONG).show()
            } else {
                val arrayList: ArrayList<Uri> = ArrayList<Uri>()
                for (i in loadFiles.indices) {
                    Log.w("msg", "whastapp add 1 ---- " + loadFiles[i])
                    Log.w("msg", "whastapp add size ---- " + loadFiles.size)
                    var imageContentUri: Uri? = null
                    try {
                        imageContentUri = Uri.fromFile(File(loadFiles[i]))
                    } catch (e: java.lang.Exception) {
                    }
                    Log.w("msg", "whastapp add ---- $imageContentUri")
                    arrayList.add(imageContentUri!!)
                    val sb = java.lang.StringBuilder()
                    sb.append("onActivityResult: ")
                    sb.append(imageContentUri)
                    Log.i("TAGGGG", sb.toString())
                }
                val obj: String = stickerName
                val stringBuilder: StringBuilder = TouchA.b(obj, "_")
                stringBuilder.append(System.currentTimeMillis())

                saveStickerPack(arrayList, obj, stringBuilder.toString())
            }
        }
    }

    private fun saveStickerPack(list: List<Uri>, str: String, str2: String) {
        Log.w("msg", "whatsapp saveStickerPack ---- $list")
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage(getString(R.string.wait_moment_while_process_your_stickers))
        progressDialog.setTitle(getString(R.string.processing_images))
        progressDialog.setProgressStyle(0)
        progressDialog.show()
        progressDialog.setCancelable(false)
        val saveStickerPackThread =
            SaveStickerPackThread(
                this,
                str,
                str2,
                list,
                progressDialog
            )
        Thread(saveStickerPackThread).start()
    }

    class SaveStickerPackThread(
        stickerDetailActivity: DownloadedStickerFullScreenActivity,
        str: String,
        str2: String,
        list: List<Uri>,
        progressDialog: ProgressDialog
    ) :
        Runnable {
        private val stickerFullScreenActivity: DownloadedStickerFullScreenActivity = stickerDetailActivity
        private val string1: String = str
        private val string2: String = str2
        private val listUri: List<Uri> = list
        private val progressDialog: ProgressDialog = progressDialog

        override fun run() {
            stickerFullScreenActivity.showProgressBar(string1, string2, listUri, progressDialog)
        }

    }

    fun showProgressBar(str: String?, str2: String?, list: List<Uri>, progressDialog: ProgressDialog) {
        try {
            val sb = java.lang.StringBuilder()
            sb.append(".")
            sb.append(WAFileUtils.generateRandomIdentifier())
            val sb2 = sb.toString()

//             moveFile(StaticFilePath.file_path + "/.emojiPack/Dinosaur/Dinosaur/",
//                     "1618540382.webp",
//                     WAConstants.STICKERS_DIRECTORY_PATH + sb2+"/");
            val array: Array<Any> = list.toTypedArray()
            array.javaClass
            val stickerPack = StickerPack(sb2, str, str2, array[0].toString(), "", "", "", "")
            stickerPack.setStickers(
                StickerPacksManager.saveStickerPackFilesLocally(
                    stickerPack.identifier,
                    list,
                    this
                )
            )
            val sb3 = java.lang.StringBuilder()
            sb3.append(getFilePath(this@DownloadedStickerFullScreenActivity))
            sb3.append(sb2)
            Log.w("msg", " whatsapp web file aa--------$sb3")
            val sb4 = sb3.toString()
            val sb5 = java.lang.StringBuilder()
            sb5.append(WAFileUtils.generateRandomIdentifier())
            sb5.append(".png")
            val sb6 = sb5.toString()
            val uri = list[0] as Uri
            val sb7 = java.lang.StringBuilder()
            sb7.append(sb4)
            sb7.append("/")
            sb7.append(sb6)
            StickerPacksManager.createStickerPackTrayIconFile(uri, Uri.parse(sb7.toString()), this)
            stickerPack.trayImageFile = sb6
            Log.w("msg", "stickerPack :: $stickerPack")
            Log.w(
                "msg",
                " StickerPacksManager.stickerPacksContainer :: " + StickerPacksManager.stickerPacksContainer
            )
            Log.w(
                "msg",
                " StickerPacksManager.stickerPacksContainer11 :: " + StickerPacksManager.stickerPacksContainer
            )
            Log.w("msg", "sb6 :: $sb6")
            StickerPacksManager.stickerPacksContainer.addStickerPack(stickerPack)
            StickerPacksManager.saveStickerPacksToJson(StickerPacksManager.stickerPacksContainer,this@DownloadedStickerFullScreenActivity)
            insertStickerPackInContentProvider(stickerPack)
            addStickerPackToWhatsApp(sb2, stickerName /*stickerPack.name*/)
        } catch (e: java.lang.Exception) {
            Log.w("msg", "aaa == $e")
            Log.w("msg", "onException onFailure extra== " + e.message)

//            e.printStackTrace();
        }
        progressDialog.dismiss()
    }

    private fun insertStickerPackInContentProvider(stickerPack: StickerPack) {
        val contentValues = ContentValues()
        contentValues.put("stickerPack", Gson().toJson(stickerPack))
        contentResolver.insert(StickerContentProvider.AUTHORITY_URI, contentValues)
    }

    private fun addStickerPackToWhatsApp(str: String, str2: String?) {
        Log.w("msg", "addStickerPackToWhatsApp == $str")
        val intent = Intent()
        intent.action = "com.whatsapp.intent.action.ENABLE_STICKER_PACK"
        intent.putExtra("sticker_pack_id", str)
        intent.putExtra(
            "sticker_pack_authority",
            "com.example.mvvmstickermodule" + ".stickercontentprovider"
        )
        intent.putExtra("sticker_pack_name", str2)
        try {
            Log.w("msg", "addStickerPackToWhatsApp try ")

//            startActivityForResult(intent, 104)
            resultLauncher.launch(intent)

        } catch (unused: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.error_adding_sticker_pack), Toast.LENGTH_LONG).show()
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
        val str = "onActivityResult: "
        val i2 = 0
        TouchA.c(str, i2, "this@DownloadedStickerFullScreenAdapter")

        if (i2 == 0) {
            if (intent != null) {
                Log.w("msg", "whastapp onActiv ---- ")
                val stringExtra = intent.getStringExtra("validation_error")
                if (stringExtra != null) {
                    Toast.makeText(this, getString(R.string.title_validation_error), Toast.LENGTH_SHORT).show()
                    val sb2 = java.lang.StringBuilder()
                    sb2.append("Validation failed:")
                    sb2.append(stringExtra)
                    Log.w("msg", sb2.toString())
                    return@registerForActivityResult
                }
                return@registerForActivityResult
            }
            Toast.makeText(this, getString(R.string.add_pack_fail), Toast.LENGTH_SHORT).show()
        } else if (i2 == -1) {
            try {
            } catch (e: java.lang.Exception) {
            }
        }
    }

    private fun shareSticker() {
        if (!isKeyboardEnabled() && !isKeyboardSet()) {
            Log.d("msg", "onClick: !isKeyboardEnabled")
            if (isKeyboardEnabled()) {
                Log.d("msg", "onClick: isKeyboardEnabled")

                PreferenceManager.saveData(this@DownloadedStickerFullScreenActivity, "isFirstSetup", false)
                SwitchDialog(this@DownloadedStickerFullScreenActivity)
                    .setDialogType(3)
                    .setAnimationEnable(true)
                    .setTitleText(getString(R.string.enable_emojikey_keyboard) + " ❤ ❤")
                    .setOtherContentText("")
                    .setPositiveListener(
                        "Go to Switch"
                    ) { dialog ->
                        dialog.dismiss()
                        val imeManager =
                            this@DownloadedStickerFullScreenActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        if (imeManager != null) {
                            imeManager.showInputMethodPicker()
                        } else {
                            Toast.makeText(
                                this@DownloadedStickerFullScreenActivity, "Error",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    .show()
            } else {
                val mHandler = SettingsPoolingHandler(
                    this@DownloadedStickerFullScreenActivity,
                    this@DownloadedStickerFullScreenActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                )
                PreferenceManager.saveData(this@DownloadedStickerFullScreenActivity, "isFirstSetup", true)
                EnableDialog(this@DownloadedStickerFullScreenActivity)
                    .setDialogType(3)
                    .setAnimationEnable(true)
                    .setTitleText(getString(R.string.enable_emojikey_keyboard) + " ❤ ❤")
                    .setOtherContentText("")
                    .setPositiveListener(
                        "Go to Enable"
                    ) { dialog ->
                        dialog.dismiss()
                        Log.w("msg", "invokeLanguageAndInputSettings== ")
                        val intent = Intent()
                        intent.action = Settings.ACTION_INPUT_METHOD_SETTINGS
                        intent.addCategory(Intent.CATEGORY_DEFAULT)
                        startActivity(intent)
                        mHandler.startPollingImeSettings()
                    }
                    .show()
            }
        } else {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }

    private fun isKeyboardEnabled(): Boolean {
        val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var list: String? = null
        try {
            list = im.enabledInputMethodList.toString()
            Log.w("msg", "KeyboardIsEnabled list $list")
        } catch (e: java.lang.Exception) {
            list = ""
        }
        return list!!.contains(applicationContext.packageName)
    }

    private fun isKeyboardSet(): Boolean {
        val id =
            Settings.Secure.getString(contentResolver, Settings.Secure.DEFAULT_INPUT_METHOD)
        val defaultInputMethod = ComponentName.unflattenFromString(id)
//        val myInputMethod: ComponentName = ComponentName(this@DownloadedStickerFullScreenAdapter, LatinIME::class.java)
//        return myInputMethod == defaultInputMethod
        return true
    }

    private inner class SettingsPoolingHandler(
        @Nonnull ownerInstance: DownloadedStickerFullScreenActivity?,
        private val mImmInHandler: InputMethodManager
    ) : LeakGuardHandlerWrapper<DownloadedStickerFullScreenActivity?>(ownerInstance!!) {
        private val MSG_POLLING_IME_SETTINGS = 0
        private val IME_SETTINGS_POLLING_INTERVAL: Long = 200
        override fun handleMessage(msg: Message) {
            val setupWizardActivity = ownerInstance ?: return
            when (msg.what) {
                MSG_POLLING_IME_SETTINGS -> {
                    if (UncachedInputMethodManagerUtils.isThisImeEnabled(
                            setupWizardActivity,
                            mImmInHandler
                        )
                    ) {
                        setupWizardActivity.invokeSetupWizardOfThisIme()
                        return
                    }
                    startPollingImeSettings()
                }
            }
        }

        fun startPollingImeSettings() {
            sendMessageDelayed(
                obtainMessage(MSG_POLLING_IME_SETTINGS),
                IME_SETTINGS_POLLING_INTERVAL
            )
        }

    }

    fun invokeSetupWizardOfThisIme() {
        val intent = Intent()
        intent.setClass(this, DownloadedStickerFullScreenActivity::class.java)
        intent.flags = (Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                or Intent.FLAG_ACTIVITY_SINGLE_TOP
                or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun deleteStickerPack() {
        try {
            val alertDialog: AlertDialog =
                AlertDialog.Builder(this@DownloadedStickerFullScreenActivity).setMessage(
                    getSpannableString(
                        Typeface.DEFAULT, R.string.download_alert2
                    )
                )
                    .setNegativeButton(getSpannableString(Typeface.DEFAULT, R.string.no),
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog?.dismiss()
                        }).setPositiveButton(getSpannableString(Typeface.DEFAULT, R.string.yes),
                        DialogInterface.OnClickListener { dialog, pos ->
                            try {
                                deletePack(File(stickerDeletePath))
                                StickerUtil.saveStickerSort(
                                    this@DownloadedStickerFullScreenActivity,
                                    DownloadedStickersFragment.downloadedStickersList
                                )
                                finish()
                            } catch (e: java.lang.Exception) {
                            }
                            dialog.dismiss()
                        }).create()
            alertDialog.show()
        } catch (e: java.lang.Exception) {
        }
    }

    private fun getSpannableString(typeface: Typeface?, i: Int): CharSequence? {
        val append = SpannableStringBuilder().append(
            SpannableString(
                resources.getString(i)
            )
        )
        return append.subSequence(0, append.length)
    }

    private fun deletePack(fileOrDirectory: File) {
        try {
            if (fileOrDirectory.isDirectory) for (child in fileOrDirectory.listFiles()) deletePack(
                child
            )
            fileOrDirectory.delete()
        } catch (e: java.lang.Exception) {
        }
    }
}