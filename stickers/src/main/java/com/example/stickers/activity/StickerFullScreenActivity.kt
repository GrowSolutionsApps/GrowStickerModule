package com.example.stickers.activity

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.*
import android.graphics.drawable.Animatable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.refactor.lib.colordialog.EnableDialog
import cn.refactor.lib.colordialog.SwitchDialog
import com.example.stickers.R
import com.example.stickers.adapter.StickerFullScreenAdapter
import com.example.stickers.databinding.ActivityStickerFullScreenBinding
import com.example.stickers.helper.FileHelper
import com.example.stickers.identities.StickerPacksContainer
import com.example.stickers.listener.StickerExpandedClickListener
import com.example.stickers.model.PreviewItem
import com.example.stickers.model.StickersListDownload
import com.example.stickers.network.CallApiDownloadData
import com.example.stickers.network.RetroService
import com.example.stickers.network.Status
import com.example.stickers.preference.PreferenceManager
import com.example.stickers.repository.StickerFullScreenRepository
import com.example.stickers.utils.*
import com.example.stickers.view.TouchA
import com.example.stickers.viewmodel.StickerFullScreenViewModel
import com.example.stickers.viewmodel.StickersFullScreenModelFactory
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.google.gson.Gson
import com.permissionx.guolindev.PermissionX
import ir.mahdi.mzip.zip.ZipArchive
import okhttp3.ResponseBody
import org.apache.commons.io.IOUtils
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import javax.annotation.Nonnull
import kotlin.math.max

class StickerFullScreenActivity : AppCompatActivity(),StickerExpandedClickListener {

    private lateinit var binding: ActivityStickerFullScreenBinding
    private lateinit var stickersFullScreenViewModel: StickerFullScreenViewModel
    private val retrofitService = RetroService.getInstance()
    private lateinit var stickerId: String
    private lateinit var stickerName: String
    private lateinit var stickerFirstImage: String
    private lateinit var stickerZip: String
    private val adapter = StickerFullScreenAdapter(this,this)
    private lateinit var directoryPath: File

    private var clickedStickerPreview: View? = null
    private val COLLAPSED_STICKER_PREVIEW_BACKGROUND_ALPHA = 1f
    private val EXPANDED_STICKER_PREVIEW_BACKGROUND_ALPHA = 0.2f
    private var expandedViewLeftX = 0f
    private var expandedViewTopY = 0f

    companion object {
        const val STICKER_ID = "stickerId"
        const val STICKER_NAME = "stickerName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Fresco.initialize(this@StickerFullScreenActivity)

        binding = ActivityStickerFullScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpUI()
        setupViewModel()
        setupData()
    }

    private fun setupViewModel() {

        stickersFullScreenViewModel = ViewModelProvider(
            this@StickerFullScreenActivity,
            StickersFullScreenModelFactory(this@StickerFullScreenActivity, StickerFullScreenRepository(retrofitService))
        )[StickerFullScreenViewModel::class.java]

        stickersFullScreenViewModel.getAllItemSelectedStickers(stickerId)
    }

    private fun setUpUI() {

        if (intent != null) {
            stickerId = intent.getStringExtra(STICKER_ID).toString()
            stickerName = intent.getStringExtra(STICKER_NAME).toString()
        }

        with(binding) {
            packName.text = stickerName

            mrlBack.setOnSingleClickListener {
                onBackPressed()
            }
            groupShareSticker.gone()
            groupWhatsApp.gone()

            textFreeEmoji.setOnSingleClickListener {
                PermissionX.init(this@StickerFullScreenActivity)
                    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .request { allGranted, _, deniedList ->
                        if (allGranted) {
                            downloadSticker()
                        } else {
                            Toast.makeText(this@StickerFullScreenActivity, "These permissions are denied: $deniedList", Toast.LENGTH_LONG).show()
                        }
                    }

            }
            viewFreeEmoji.setOnSingleClickListener {
                PermissionX.init(this@StickerFullScreenActivity)
                    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .request { allGranted, _, deniedList ->
                        if (allGranted) {
                            downloadSticker()
                        } else {
                            Toast.makeText(this@StickerFullScreenActivity, "These permissions are denied: $deniedList", Toast.LENGTH_LONG).show()
                        }
                    }
        }
            textAddToWhatsApp.setOnSingleClickListener {
                PermissionX.init(this@StickerFullScreenActivity)
                    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .request { allGranted, _, deniedList ->
                        if (allGranted) {
                            addPackToWhatsApp()
                        } else {
                            Toast.makeText(this@StickerFullScreenActivity, "These permissions are denied: $deniedList", Toast.LENGTH_LONG).show()
                        }
                    }

            }
            viewWhatsapp.setOnSingleClickListener {
                PermissionX.init(this@StickerFullScreenActivity)
                    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .request { allGranted, _, deniedList ->
                        if (allGranted) {
                            addPackToWhatsApp()
                        } else {
                            Toast.makeText(this@StickerFullScreenActivity, "These permissions are denied: $deniedList", Toast.LENGTH_LONG).show()
                        }
                    }

            }
            textShareSticker.setOnSingleClickListener {
                PermissionX.init(this@StickerFullScreenActivity)
                    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .request { allGranted, _, deniedList ->
                        if (allGranted) {
                            shareSticker()
                        } else {
                            Toast.makeText(this@StickerFullScreenActivity, "These permissions are denied: $deniedList", Toast.LENGTH_LONG).show()
                        }
                    }

            }
            viewShareSticker.setOnSingleClickListener {
                PermissionX.init(this@StickerFullScreenActivity)
                    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .request { allGranted, _, deniedList ->
                        if (allGranted) {
                            shareSticker()

                        } else {
                            Toast.makeText(this@StickerFullScreenActivity, "These permissions are denied: $deniedList", Toast.LENGTH_LONG).show()
                        }
                    }

            }

            setBottomButtons()

        }


    }

    private fun setBottomButtons() {

        directoryPath = File(getStickerDownloadPath(this@StickerFullScreenActivity) + "/")

        val existingPath = File(directoryPath.absolutePath + File.separator + stickerName)
        with(binding) {

            if (existingPath.exists()) {
                Log.w("msg", "existingPath: $existingPath")
                groupFreeEmoji.gone()
                groupWhatsApp.visible()
                groupShareSticker.visible()

            } else {
                Log.w("msg", "existingPath does not exist")
                groupFreeEmoji.visible()
                groupWhatsApp.gone()
                groupShareSticker.gone()
            }
        }
    }

    private fun downloadSticker() {

//        if (StickerPacksManager.stickerPacksContainer == null) {
//            val str = ""
//            StickerPacksManager.stickerPacksContainer = StickerPacksContainer(
//                str,
//                str,
//                StickerPacksManager.getStickerPacks(this@StickerFullScreenActivity)
//            )
//        }
//        WAFileUtils.initializeDirectories(this@StickerFullScreenActivity)

//        directoryPath = File(getStickerFilePath(this) + "/")
        directoryPath = File(getStickerDownloadPath(this) + "/")
        if (!directoryPath.exists()) {
            directoryPath.mkdir()
        }
        if (isOnline()) {


            with(binding) {
//                if (!directoryPath.exists()) {
//                    directoryPath.mkdirs()
//                }
                Log.w("msg", "downloadSticker: $stickerZip")
                 if (!File("$stickerName.zip").exists() && !File(directoryPath.absolutePath + "/" + stickerName).exists()) {
                     Log.w("msg", "downloadSticker zip : $stickerName.zip")
                     Log.w("msg", "downloadSticker absolutePath : " + directoryPath.absolutePath + "/" + stickerName)

                     if (!File(directoryPath.absolutePath + "/" + stickerName).exists()) {
                            groupFreeEmoji.visible()
                            textFreeEmoji.text = getString(R.string.extracting)
                      } else {
                          textFreeEmoji.text = getString(R.string.done)
                      }
                     CallApiDownloadData.downloadFileWithDynamicUrlSync(
                         this@StickerFullScreenActivity,
                         stickerZip,
                         object : CallApiDownloadData.CallBack {
                             override fun onLoaded(response: Response<ResponseBody?>?) {
                                 Log.w("msg", "sticker onResponse-- $response")
                                 try {
                                     val zip: String = directoryPath.absolutePath + File.separator + stickerName + ".zip"
                                     Log.w("msg", "onLoaded: $zip")
                                     val zipFile = File(zip)
                                     Log.w("msg", "onLoaded zipFile ::: $zipFile")
                                     val fileOutputStream = FileOutputStream(zipFile)
                                     Log.w("msg", "onLoaded zipFile :: $zipFile")

                                     IOUtils.write(response!!.body()!!.bytes(), fileOutputStream)

                                     afterDownloadComplete()

                                 } catch (e1: Exception) {
                                     Log.w("msg", "onException onFailure == " + e1.message)
                                 }
                             }

                             override fun onServerError() {
                                 if (!File(directoryPath.absolutePath + File.separator + stickerName).exists()) {
                                     textFreeEmoji.text = getString(R.string.free_emoji)
                                     Toast.makeText(
                                         this@StickerFullScreenActivity,
                                         getString(R.string.download_fail_retry),
                                         Toast.LENGTH_SHORT
                                     ).show()
                                 } else {
                                     textFreeEmoji.text = getString(R.string.done)
                                 }
                             }

                             override fun onNetworkError() {
                                 if (!File(directoryPath.absolutePath + File.separator + stickerName).exists()) {
                                     textFreeEmoji.text = getString(R.string.free_emoji)
                                     Toast.makeText(
                                         this@StickerFullScreenActivity,
                                         getString(R.string.download_fail_retry),
                                         Toast.LENGTH_SHORT
                                     ).show()
                                 } else {
                                     textFreeEmoji.text = getString(R.string.done)
                                 }
                             }
                         })
                 }
            }
        } else {
            Toast.makeText(this@StickerFullScreenActivity, getString(R.string.no_internet_try_again), Toast.LENGTH_LONG).show()

        }
    }

    private fun afterDownloadComplete() {

        if (!File(directoryPath.absolutePath + File.separator + stickerName).exists()) {
            binding.textFreeEmoji.text = getString(R.string.extracting)
            binding.groupFreeEmoji.visible()

            val pwd = "4a07add16334f941af670604b1181dd6"
//            Log.w("msg", "api_key== " + Utils.remoteConfig.getString(StaticData.api_key))
//            if (!Utils.remoteConfig.getString(StaticData.api_key).equals("")) {
//                pwd = Utils.remoteConfig.getString(StaticData.api_key)
//            }
            ZipArchive.unzip(
                directoryPath.absolutePath + File.separator + stickerName + ".zip",
                directoryPath.absolutePath, pwd)

            try {
                Handler(Looper.getMainLooper()).postDelayed({
                    try {
                        val file = File(directoryPath.absolutePath + File.separator + stickerName + ".zip")
                        file.delete()
                        Toast.makeText(
                            this@StickerFullScreenActivity,
                            "Pack $stickerName Downloaded..", Toast.LENGTH_SHORT
                        ).show()
//                        button_download.setText(getString(R.string.add_to_whatsapp))
                        binding.groupWhatsApp.visible()
                        binding.groupShareSticker.visible()
                        binding.groupFreeEmoji.gone()


                    } catch (e: java.lang.Exception) {
                        binding.textFreeEmoji.text = (getString(R.string.download))
                        Toast.makeText(
                            this@StickerFullScreenActivity,
                            getString(R.string.download_fail_retry),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, 1000)
            } catch (e: java.lang.Exception) {
                if (!File(directoryPath.absolutePath + File.separator + stickerName).exists()) {
                    binding.groupFreeEmoji.visible()
                    binding.textFreeEmoji.text = getString(R.string.download)
                    Toast.makeText(
                        this@StickerFullScreenActivity,
                        getString(R.string.download_fail_retry),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    binding.groupFreeEmoji.visible()
                    binding.textFreeEmoji.text = getString(R.string.done)
                    val file = File(directoryPath.absolutePath + File.separator + stickerName + ".zip")
                    file.delete()
                }
            }
        } else {
            if (!File(directoryPath.absolutePath + File.separator + stickerName).exists()) {
                binding.groupFreeEmoji.visible()
                binding.textFreeEmoji.text = getString(R.string.done)
            } else {
                binding.groupFreeEmoji.visible()
                binding.textFreeEmoji.text = getString(R.string.done)
            }
        }
    }

    private fun setupData() {

        //set adapter in recyclerview
        binding.rvAllStickers.adapter = adapter

        //the observer will only receive events if the owner(activity) is in active state
        //invoked when allStickersList data changes
        stickersFullScreenViewModel.allStickersList.observe(this) {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        binding.noNetwork.clNoNetwork.gone()
                        binding.progressBar.visible()
                        binding.rvAllStickers.gone()

                    }
                    Status.NO_INTERNET -> {
                        binding.noNetwork.clNoNetwork.visible()
                        binding.progressBar.gone()
                        binding.rvAllStickers.gone()
                    }
                    Status.SUCCESS -> {
                        binding.rvAllStickers.visible()
                        binding.noNetwork.clNoNetwork.gone()
                        binding.progressBar.gone()

                        it.data?.let { it1 ->
                            adapter.setAllStickerList(it1.preview)
                            if (it1.preview.isNotEmpty()) {
                                stickerFirstImage = it1.preview[0].image
                                stickerZip = it1.zip
                            }
                        }

                        setupFirstImage()

                    }
                    Status.ERROR -> {
                        binding.progressBar.gone()
                        binding.rvAllStickers.gone()
                        binding.noNetwork.clNoNetwork.gone()

                    }

                }
                Log.d("TAG", "onlinestickersList: $it")

            } else {
                Toast.makeText(this, "Error in fetching data", Toast.LENGTH_SHORT).show()
            }
        }

        //invoked when a network exception occurred
        stickersFullScreenViewModel.errorMessage.observe(this) {
            Log.d("TAG", "errorMessage: $it")
        }

    }

    private fun setupFirstImage() {

        binding.trayImage.setImageURI(stickerFirstImage)


        val imageUri = Uri.parse(stickerFirstImage)


        val imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(imageUri)
        val builder = Fresco.newDraweeControllerBuilder()
        builder.imageRequest = imageRequestBuilder.build()
        builder.autoPlayAnimations = true
        builder.controllerListener = object : BaseControllerListener<ImageInfo?>() {
            override fun onFinalImageSet(
                id: String,
                imageInfo: ImageInfo?,
                animatable: Animatable?
            ) {
            }
        }
        binding.trayImage.controller = builder.build()
        binding.trayImage.hierarchy.setFailureImage(R.drawable.sticker_error)
        binding.trayImage.hierarchy.setRetryImage(R.drawable.sticker_error)
        binding.trayImage.hierarchy.setPlaceholderImage(R.drawable.sticker_error)

    }

    override fun onClickExpand(
        position: Int,
        stickerItemPreview: View,
        allStickers: MutableList<PreviewItem>
    ) {
        this.clickedStickerPreview = stickerItemPreview

        with(binding) {
            if (isStickerPreviewExpanded()) {
                hideExpandedStickerPreview()
                return
            }
            if (!stickerDetailsExpandedSticker.equals(null)) {
                positionExpandedStickerPreview(position)
                Log.w("msg", "sticker full list---  $allStickers[position].image")
                val controller: DraweeController = Fresco.newDraweeControllerBuilder()
                    .setUri(allStickers[position].image)
                    .setAutoPlayAnimations(true)
                    .build()
                with(stickerDetailsExpandedSticker) {
                    setImageResource(R.drawable.sticker_error)
                    setController(controller)
                    visible()
                }
                rvAllStickers.alpha = EXPANDED_STICKER_PREVIEW_BACKGROUND_ALPHA
                stickerDetailsExpandedSticker.setOnSingleClickListener {
                    hideExpandedStickerPreview()
                }
            }
        }
    }

    override fun onDownloadedClickExpand(
        position: Int,
        stickerItemPreview: View,
        allStickers: MutableList<StickersListDownload>
    ) {

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
            val clickedViewHolder: StickerFullScreenAdapter.AllStickerViewHolder =
                rvAllStickers.findViewHolderForAdapterPosition(selectedPosition) as StickerFullScreenAdapter.AllStickerViewHolder
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
                StickerPacksManager.getStickerPacks(this@StickerFullScreenActivity)
            )
        }
        WAFileUtils.initializeDirectories(this@StickerFullScreenActivity)

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
        stickerDetailActivity: StickerFullScreenActivity,
        str: String,
        str2: String,
        list: List<Uri>,
        progressDialog: ProgressDialog
    ) :
        Runnable {
        private val stickerFullScreenActivity: StickerFullScreenActivity = stickerDetailActivity
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
            val array: Array<Any> = list.toTypedArray()
            array.javaClass
            val stickerPack = StickerPack(sb2, str, str2, array[0].toString(), "", "", "", "")
            Log.w("msg", "whatsapp StickerPack: " + array[0].toString())
            stickerPack.setStickers(
                StickerPacksManager.saveStickerPackFilesLocally(
                    stickerPack.identifier,
                    list,
                    this
                )
            )
            val sb3 = java.lang.StringBuilder()
            sb3.append(getFilePath(this@StickerFullScreenActivity))
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
            StickerPacksManager.saveStickerPacksToJson(StickerPacksManager.stickerPacksContainer,this@StickerFullScreenActivity)
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
//            resultLauncher.launch(intent)
            someActivityResultLauncher.launch(intent)

        } catch (unused: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.error_adding_sticker_pack), Toast.LENGTH_LONG).show()
        }
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    private var someActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode === Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val str = "onActivityResult: "
            val i2 = 0
//            TouchA.c(str, i2, "this@StickerFullScreenActivity")
            Log.w("msg", "whatsapp start activity ---- ")

            if (i2 == 0) {
                if (intent != null) {
                    Log.w("msg", "whastapp onActiv ---- ")
                    val stringExtra = intent.getStringExtra("validation_error")
                    Log.w("msg", "whastapp onActiv stringExtra---- " + stringExtra)

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
                Log.w("msg","onActivityResult i2 : "+i2)
                try {
                } catch (e: java.lang.Exception) {
                }
            }
        }
    }

//    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
//            val str = "onActivityResult: "
//            val i2 = 0
//            TouchA.c(str, i2, "this@StickerFullScreenActivity")
//        Log.w("msg", "whatsapp start activity ---- ")
//
//            if (i2 == 0) {
//                if (intent != null) {
//                    Log.w("msg", "whastapp onActiv ---- ")
//                    val stringExtra = intent.getStringExtra("validation_error")
//                    if (stringExtra != null) {
//                        Toast.makeText(this, getString(R.string.title_validation_error), Toast.LENGTH_SHORT).show()
//                        val sb2 = java.lang.StringBuilder()
//                        sb2.append("Validation failed:")
//                        sb2.append(stringExtra)
//                        Log.w("msg", sb2.toString())
//                        return@registerForActivityResult
//                    }
//                    return@registerForActivityResult
//                }
//                Toast.makeText(this, getString(R.string.add_pack_fail), Toast.LENGTH_SHORT).show()
//            } else if (i2 == -1) {
//                Log.w("msg","onActivityResult i2 : "+i2)
//                try {
//                } catch (e: java.lang.Exception) {
//                }
//            }
//    }

    private fun shareSticker() {
        if (!isKeyboardEnabled() && !isKeyboardSet()) {
            Log.d("msg", "onClick: !isKeyboardEnabled")
            if (isKeyboardEnabled()) {
                Log.d("msg", "onClick: isKeyboardEnabled")

                PreferenceManager.saveData(this@StickerFullScreenActivity, "isFirstSetup", false)
                SwitchDialog(this@StickerFullScreenActivity)
                    .setDialogType(3)
                    .setAnimationEnable(true)
                    .setTitleText(getString(R.string.enable_emojikey_keyboard) + " ❤ ❤")
                    .setOtherContentText("")
                    .setPositiveListener(
                        "Go to Switch"
                    ) { dialog ->
                        dialog.dismiss()
                        val imeManager =
                            this@StickerFullScreenActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        if (imeManager != null) {
                            imeManager.showInputMethodPicker()
                        } else {
                            Toast.makeText(
                                this@StickerFullScreenActivity, "Error",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    .show()
            } else {
                val mHandler = SettingsPoolingHandler(
                    this@StickerFullScreenActivity,
                    this@StickerFullScreenActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                )
                PreferenceManager.saveData(this@StickerFullScreenActivity, "isFirstSetup", true)
                EnableDialog(this@StickerFullScreenActivity)
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
//            imm.showSoftInput(binding.viewShareSticker, 0)
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
//        val myInputMethod: ComponentName = ComponentName(this@StickerFullScreenActivity, LatinIME::class.java)
//        return myInputMethod == defaultInputMethod
        return true
    }

    private inner class SettingsPoolingHandler(
        @Nonnull ownerInstance: StickerFullScreenActivity?,
        private val mImmInHandler: InputMethodManager
    ) : LeakGuardHandlerWrapper<StickerFullScreenActivity?>(ownerInstance!!) {
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
        intent.setClass(this, StickerFullScreenActivity::class.java)
        intent.flags = (Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                or Intent.FLAG_ACTIVITY_SINGLE_TOP
                or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}