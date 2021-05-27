package com.codility.directreplynotification

import android.app.*
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import kotlin.random.Random
import android.service.notification.StatusBarNotification
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import android.util.Log
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import java.io.*


@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {

    var count = 0
    val prefName = "MyPref"
    val prefNotificationKey = "notification"
    lateinit var sharedPreferences : SharedPreferences
    var imgPath : String = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        * So, from Android Nougat, creating a notification channel is compulsory for displaying notifications.
        * Inside onCreate(), we will check if the device version is Android N or greater we will create a notification channel.
        * */
        //clearPref()
        sharedPreferences = getSharedPreferences(prefName,Context.MODE_PRIVATE)
        clearPref()
        createNotificationChannel()
        loadImg()

        Handler().postDelayed(Runnable { chooseImage() },3000)
    }

    private fun loadImg() {
        Glide.with(this)
                .load("https://s3.amazonaws.com/socialapplicationuploads/post_images/600e8674402f2.jpg")
                //.load("https://s3.amazonaws.com/socialapplicationuploads/post_images/600e860d433a8.jpg")
                .into(image1)
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_HIGH

            val gid1 = NotificationChannelGroup("1","ABC")
            val mChannel = NotificationChannel(ConstantResource.CHANNEL_ID, ConstantResource.CHANNEL_NAME, importance)
            mChannel.description = ConstantResource.NOTIFICATION_DESCRIPTION
            mChannel.enableLights(true)
            mChannel.lightColor = Color.MAGENTA
            mChannel.group = "1"
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            mNotificationManager.createNotificationChannelGroup(gid1)
            mNotificationManager.createNotificationChannel(mChannel)

            val gid2 = NotificationChannelGroup("2","XYZ")
            val mChannel1 = NotificationChannel(ConstantResource.CHANNEL_ID1, ConstantResource.CHANNEL_NAME1, importance)
            mChannel1.description = ConstantResource.NOTIFICATION_DESCRIPTION1
            mChannel1.enableLights(true)
            mChannel1.lightColor = Color.MAGENTA
            mChannel1.group = "2"
            mChannel1.enableVibration(true)
            mChannel1.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            mNotificationManager.createNotificationChannelGroup(gid2)
            mNotificationManager.createNotificationChannel(mChannel1)

            val gid3 = NotificationChannelGroup("3","XYZ1")
            val mChannel2 = NotificationChannel(ConstantResource.CHANNEL_ID2, ConstantResource.CHANNEL_NAME2, importance)
            mChannel2.description = ConstantResource.NOTIFICATION_DESCRIPTION1
            mChannel2.enableLights(true)
            mChannel2.lightColor = Color.MAGENTA
            mChannel2.group = "3"
            mChannel2.enableVibration(true)
            mChannel2.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            mNotificationManager.createNotificationChannelGroup(gid3)
            mNotificationManager.createNotificationChannel(mChannel2)
        }
    }

    fun showNotificationView(view: View) {
        displayNotification()
        //displayNotification1()

        //1-SAURAV,2-MAYUR,3-THAKKAR,4-HALANI
        val SENDER_NAME = etSenderName.text.toString().toUpperCase()
        var SENDER_ID = etSenderId.text.toString().toUpperCase()
        //messageStyleNotification(SENDER_NAME,SENDER_ID)
        //crop()
    }


    private fun displayNotification1() {
        val SUMMARY_ID = 0
        val SUMMARY_ID1 = 101
        val SUMMARY_ID2 = 102
        val SUMMARY_ID3 = 103
        val GROUP_KEY_WORK_EMAIL = "com.android.example.WORK_EMAIL"
        val GROUP_KEY_WORK_MOBILE = "com.android.example.WORK_MOBILE"
        val GROUP_KEY_WORK_MOBILE1 = "com.android.example.WORK_MOBILE1"
        val GROUP_KEY_WORK_MOBILE2 = "com.android.example.WORK_MOBILE2"

        val newMessageNotification1 = NotificationCompat.Builder(this@MainActivity, ConstantResource.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Saurav")
                .setContentText("You will not believe...")
                .setGroup(GROUP_KEY_WORK_EMAIL)
                .build()

        val newMessageNotification2 = NotificationCompat.Builder(this@MainActivity, ConstantResource.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Saurav")
                .setContentText("Please join us to celebrate the...")
                .setGroup(GROUP_KEY_WORK_EMAIL)
                .build()

        val newMessageNotification3 = NotificationCompat.Builder(this@MainActivity, ConstantResource.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Saurav")
                .setContentText("Please join us to celebrate the...1")
                .setGroup(GROUP_KEY_WORK_EMAIL)
                .build()

        val newMessageNotification4 = NotificationCompat.Builder(this@MainActivity, ConstantResource.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Saurav")
                .setContentText("Please join us to celebrate the...2")
                .setGroup(GROUP_KEY_WORK_EMAIL)
                .build()

        val summaryNotification = NotificationCompat.Builder(this@MainActivity, ConstantResource.CHANNEL_ID)
                .setContentTitle("Thakkar")
                //set content text to support devices running API level < 24
                .setContentText("Two new messages")
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                //build summary info into InboxStyle template
                .setStyle(NotificationCompat.InboxStyle()
                        .addLine("Alex Faarborg  Check this out")
                        .addLine("Jeff Chang    Launch Party")
                        .setBigContentTitle("2 new messages")
                        .setSummaryText("janedoe@example.com"))
                //specify which group this notification belongs to
                .setGroup(GROUP_KEY_WORK_EMAIL)
                //set this notification as the summary for the group
                .setGroupSummary(true)
                .build()

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(1, newMessageNotification1)
        notificationManager.notify(2, newMessageNotification2)
        notificationManager.notify(3, newMessageNotification3)
        notificationManager.notify(4, newMessageNotification4)
        notificationManager.notify(SUMMARY_ID, summaryNotification)

        val morePendingIntent = PendingIntent.getBroadcast(this, ConstantResource.REQUEST_CODE_MORE, Intent(this, MyNotificationReceiver::class.java)
                .putExtra(ConstantResource.KEY_MORE, ConstantResource.REQUEST_CODE_MORE), PendingIntent.FLAG_UPDATE_CURRENT)

        val newMessageNotification5 = NotificationCompat.Builder(this@MainActivity, ConstantResource.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Mayur")
                .setContentText("You will not believe...")
                .setGroup(GROUP_KEY_WORK_MOBILE)
                .addAction(android.R.drawable.ic_menu_compass, "More", morePendingIntent)
                .build()

        val newMessageNotification6 = NotificationCompat.Builder(this@MainActivity, ConstantResource.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Mayur")
                .setContentText("Please join us to celebrate the...")
                .setGroup(GROUP_KEY_WORK_MOBILE)
                .addAction(android.R.drawable.ic_menu_compass, "More", morePendingIntent)
                .build()

        val newMessageNotification7 = NotificationCompat.Builder(this@MainActivity, ConstantResource.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Mayur")
                .setContentText("Please join us to celebrate the...1")
                .setGroup(GROUP_KEY_WORK_MOBILE)
                .addAction(android.R.drawable.ic_menu_compass, "More", morePendingIntent)
                .build()

        val newMessageNotification8 = NotificationCompat.Builder(this@MainActivity, ConstantResource.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Mayur")
                .setContentText("Please join us to celebrate the...2")
                .setGroup(GROUP_KEY_WORK_MOBILE)
                .addAction(android.R.drawable.ic_menu_compass, "More", morePendingIntent)
                .build()

        val summaryNotification1 = NotificationCompat.Builder(this@MainActivity, ConstantResource.CHANNEL_ID)
                .setContentTitle("Thakkar1")
                //set content text to support devices running API level < 24
                .setContentText("Two new messages")
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                //build summary info into InboxStyle template
                .setStyle(NotificationCompat.InboxStyle()
                        .addLine("Alex Faarborg  Check this out")
                        .addLine("Jeff Chang    Launch Party")
                        .setBigContentTitle("2 new messages")
                        .setSummaryText("janedoe@example.com"))
                //specify which group this notification belongs to
                .setGroup(GROUP_KEY_WORK_MOBILE)
                .addAction(android.R.drawable.ic_menu_compass, "More", morePendingIntent)
                //set this notification as the summary for the group
                .setGroupSummary(true)
                .build()


        val notificationManager1 = NotificationManagerCompat.from(this)
        notificationManager1.notify(5, newMessageNotification5)
        notificationManager1.notify(6, newMessageNotification6)
        notificationManager1.notify(7, newMessageNotification7)
        notificationManager1.notify(8, newMessageNotification8)
        notificationManager1.notify(SUMMARY_ID1, summaryNotification1)

        val newMessageNotification9 = NotificationCompat.Builder(this@MainActivity, ConstantResource.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Saurav")
                .setContentText("You will not believe...")
                .setGroup(GROUP_KEY_WORK_MOBILE1)
                .build()

        val newMessageNotification10 = NotificationCompat.Builder(this@MainActivity, ConstantResource.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Saurav")
                .setContentText("Please join us to celebrate the...")
                .setGroup(GROUP_KEY_WORK_MOBILE1)
                .build()

        val newMessageNotification11 = NotificationCompat.Builder(this@MainActivity, ConstantResource.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Saurav")
                .setContentText("Please join us to celebrate the...1")
                .setGroup(GROUP_KEY_WORK_MOBILE1)
                .build()

        val newMessageNotification12 = NotificationCompat.Builder(this@MainActivity, ConstantResource.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Saurav")
                .setContentText("Please join us to celebrate the...2")
                .setGroup(GROUP_KEY_WORK_MOBILE1)
                .build()

        val summaryNotification2 = NotificationCompat.Builder(this@MainActivity, ConstantResource.CHANNEL_ID)
                .setContentTitle("Thakkar")
                //set content text to support devices running API level < 24
                .setContentText("Two new messages")
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                //build summary info into InboxStyle template
                .setStyle(NotificationCompat.InboxStyle()
                        .addLine("Alex Faarborg  Check this out")
                        .addLine("Jeff Chang    Launch Party")
                        .setBigContentTitle("2 new messages")
                        .setSummaryText("janedoe@example.com"))
                //specify which group this notification belongs to
                .setGroup(GROUP_KEY_WORK_MOBILE1)
                //set this notification as the summary for the group
                .setGroupSummary(true)
                .build()

        val notificationManager2 = NotificationManagerCompat.from(this)
        notificationManager2.notify(9, newMessageNotification9)
        notificationManager2.notify(10, newMessageNotification10)
        notificationManager2.notify(11, newMessageNotification11)
        notificationManager2.notify(12, newMessageNotification12)
        notificationManager2.notify(SUMMARY_ID2, summaryNotification2)

        val newMessageNotification13 = NotificationCompat.Builder(this@MainActivity, ConstantResource.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Saurav")
                .setContentText("You will not believe...")
                .setGroup(GROUP_KEY_WORK_MOBILE2)
                .build()

        val newMessageNotification14 = NotificationCompat.Builder(this@MainActivity, ConstantResource.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Saurav")
                .setContentText("Please join us to celebrate the...")
                .setGroup(GROUP_KEY_WORK_MOBILE2)
                .build()

        val newMessageNotification15 = NotificationCompat.Builder(this@MainActivity, ConstantResource.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Saurav")
                .setContentText("Please join us to celebrate the...1")
                .setGroup(GROUP_KEY_WORK_MOBILE2)
                .build()

        val newMessageNotification16 = NotificationCompat.Builder(this@MainActivity, ConstantResource.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Saurav")
                .setContentText("Please join us to celebrate the...2")
                .setGroup(GROUP_KEY_WORK_MOBILE2)
                .build()

        val summaryNotification3 = NotificationCompat.Builder(this@MainActivity, ConstantResource.CHANNEL_ID)
                .setContentTitle("Thakkar")
                //set content text to support devices running API level < 24
                .setContentText("Two new messages")
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                //build summary info into InboxStyle template
                .setStyle(NotificationCompat.InboxStyle()
                        .addLine("Alex Faarborg  Check this out")
                        .addLine("Jeff Chang    Launch Party")
                        .setBigContentTitle("2 new messages")
                        .setSummaryText("janedoe@example.com"))
                //specify which group this notification belongs to
                .setGroup(GROUP_KEY_WORK_MOBILE2)
                //set this notification as the summary for the group
                .setGroupSummary(true)
                .build()

        val notificationManager3 = NotificationManagerCompat.from(this)
        notificationManager3.notify(13, newMessageNotification13)
        notificationManager3.notify(14, newMessageNotification14)
        notificationManager3.notify(15, newMessageNotification15)
        notificationManager3.notify(16, newMessageNotification16)
        notificationManager3.notify(SUMMARY_ID3, summaryNotification3)

    }

    private fun messageStyleNotification(SENDER_NAME : String,SENDER_ID : String) {

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val style = NotificationCompat.MessagingStyle("")//.setConversationTitle("Q&A Group")

        if(getNotification() != ""){
            var notiType = object : TypeToken<List<Notifications>>() {}.type
            var list = Gson().fromJson<List<Notifications>>(getNotification(), notiType) as ArrayList<Notifications>
            for(i in list.indices){
                if(list[i].SENDER_ID == SENDER_ID){
                    style.addMessage(list[i].MSG,System.currentTimeMillis(),list[i].SENDER)
                    notificationManager.cancel(list[i].ID)
                }
            }
        }

        style.addMessage("Hi"+count,System.currentTimeMillis(),SENDER_NAME)


        val launchIntent = getLaunchIntent(count, baseContext)
        val builder = NotificationCompat.Builder(this,ConstantResource.CHANNEL_ID2)
        builder.setSmallIcon(android.R.drawable.ic_dialog_email)
        builder.setLargeIcon(BitmapFactory.decodeResource(resources, android.R.drawable.ic_dialog_email))
        builder.setContentTitle("Messages")
        builder.setStyle(style)
        builder.setAutoCancel(true)
        builder.setGroup("GROUP")
        builder.setContentIntent(launchIntent)

        notificationManager.notify(count, builder.build())

        val notificationObj = createString(SENDER_NAME,SENDER_ID,"Hi"+count,count)
        saveNotification(notificationObj)
        count++

        /*--------------------------------------------------------------------------*/
        val summaryNotification = NotificationCompat.Builder(this@MainActivity, ConstantResource.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setStyle(NotificationCompat.InboxStyle()
                        .setSummaryText("Messages"))
                .setGroup("GROUP")
                .setGroupSummary(true)
                //.setContentIntent(launchIntent)
                .build()

        //notificationManager.notify(5000000, summaryNotification)

    }

    fun getLaunchIntent(notificationId: Int, context: Context): PendingIntent {

        val intent = Intent(context, MyNotificationReceiver::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("notificationId", notificationId)
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }

    private fun displayNotification() {
        //Pending intent for a notification button named More
        val morePendingIntent = PendingIntent.getBroadcast(this, ConstantResource.REQUEST_CODE_MORE, Intent(this, MyNotificationReceiver::class.java)
                .putExtra(ConstantResource.KEY_MORE, ConstantResource.REQUEST_CODE_MORE), PendingIntent.FLAG_UPDATE_CURRENT)

        //Pending intent for a notification button help
        val helpPendingIntent = PendingIntent.getBroadcast(this, ConstantResource.REQUEST_CODE_HELP, Intent(this, MyNotificationReceiver::class.java)
                .putExtra(ConstantResource.KEY_HELP, ConstantResource.REQUEST_CODE_HELP), PendingIntent.FLAG_UPDATE_CURRENT)

        //Pending intent for a notification button reply
        val replyPendingIntent = PendingIntent.getActivity(this, ConstantResource.REQUEST_CODE_SEND, Intent(this, MyNotificationReceiver::class.java)
                .putExtra(ConstantResource.KEY_SEND,ConstantResource.REQUEST_CODE_SEND), PendingIntent.FLAG_UPDATE_CURRENT)

        //We need this object for getting direct input from notification
        val remoteInput = RemoteInput.Builder(ConstantResource.NOTIFICATION_REPLY)
                .setLabel("Please enter your message")
                .build()

        //For the remote input we need this action object
        val action = NotificationCompat.Action.Builder(android.R.drawable.ic_delete, "REPLY", replyPendingIntent)
                .addRemoteInput(remoteInput)
                .build()

        val style = NotificationCompat.InboxStyle()
        style.addLine("Kem 6o")
        style.addLine("Su kre")

        val channelId = when(count) {1->ConstantResource.CHANNEL_ID 2->ConstantResource.CHANNEL_ID1 else->ConstantResource.CHANNEL_ID2}
        //Creating the notifiction builder object
        //val mBuilder = NotificationCompat.Builder(this, if(count%2==0) ConstantResource.CHANNEL_ID else ConstantResource.CHANNEL_ID1)
        val mBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle(when(count){ 1->"Saurav" 2->"Mayur" else->"Thakkar" })
                //.setContentTitle(if(count %2 == 0)"Saurav" else "Mayur")
                .setContentText("Hey"+count)
                .setAutoCancel(true)
                .setContentIntent(helpPendingIntent)
                .addAction(action)
                .setGroup("TEST")
                .setGroupSummary(true)
        //.addAction(android.R.drawable.ic_menu_compass, "More", morePendingIntent)
        //.addAction(android.R.drawable.ic_menu_directions, "Help", helpPendingIntent)

        //finally displaying the notification
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //val id = java.util.Random().nextInt(1000)
        val id = when(count) {1->1 2->2 else->3}
        notificationManager.notify(/*ConstantResource.NOTIFICATION_ID*/id, mBuilder.build())

        count++
    }


    private fun createString(sender: String,senderId: String, msg: String, notificationId: Int) : String {
        if(getNotification() == ""){

            var list = ArrayList<Notifications>()
            val obj = Notifications(sender,senderId,msg,notificationId)
            list.add(obj)
            val data = Gson().toJson(list)
            return data
        }else{
            var notiType = object : TypeToken<List<Notifications>>() {}.type
            var list = Gson().fromJson<List<Notifications>>(getNotification(), notiType) as ArrayList<Notifications>

            val obj = Notifications(sender,senderId,msg,notificationId)
            list.add(obj)
            val data = Gson().toJson(list)
            return data
        }

    }

    private fun saveNotification(obj : String){

        val editor = sharedPreferences.edit()
        editor.putString(prefNotificationKey,obj)
        editor.apply()
        editor.commit()
    }

    private fun getNotification() : String?{
        return sharedPreferences.getString(prefNotificationKey,"")
    }

    private fun clearPref(){
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    private fun crop(){
         try {

                var paint = Paint()
                paint.setFilterBitmap(true);
                var bitmapOrg = BitmapFactory.decodeResource(
                        getResources(),
                        R.drawable.temp);

                var targetWidth = bitmapOrg.getWidth();
                var targetHeight = bitmapOrg.getHeight();

                var targetBitmap = Bitmap.createBitmap(targetWidth,
                        targetHeight, Bitmap.Config.ARGB_8888);

                var rectf = RectF(0f, 50f, bitmapOrg.getWidth().toFloat(), 545f)

                var canvas = Canvas(targetBitmap);
                var path = Path();

                path.addRect(rectf, Path.Direction.CW);
                canvas.clipPath(path);

                canvas.drawBitmap(
                        bitmapOrg,
                        Rect(0, 0, bitmapOrg.getWidth(), bitmapOrg
                                .getHeight()), Rect(0, 0, targetWidth,
                                targetHeight), paint);

                var matrix = Matrix();
                matrix.postScale(1f, 1f);

                var bitmapFatoryOptions = BitmapFactory.Options();
                bitmapFatoryOptions.inPreferredConfig = Bitmap.Config.RGB_565;

                bitmapOrg = BitmapFactory.decodeResource(getResources(),
                        R.drawable.temp,
                        bitmapFatoryOptions);

             Log.e("f","df")

            } catch ( e:Exception) {
                System.out.println("Error1 : " + e.message
                        + e.toString());
            }
    }


    //MediaStore API
    private fun saveImageToStorage(
            bitmap: Bitmap,
            filename: String = "screenshot.jpg",
            mimeType: String =  "image/jpeg",
            directory: String = Environment.DIRECTORY_PICTURES,
            mediaContentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    ) {
        val imageOutStream: OutputStream
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                put(MediaStore.Images.Media.RELATIVE_PATH, directory)
            }

            contentResolver.run {
                val uri =
                        contentResolver.insert(mediaContentUri, values)
                                ?: return
                imageOutStream = openOutputStream(uri) ?: return
            }
        } else {
            val imagePath = Environment.getExternalStoragePublicDirectory(directory).absolutePath
            val image = File(imagePath, filename)
            imageOutStream = FileOutputStream(image)
        }

        imageOutStream.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
    }

    private fun chooseImage() {
        startActivityForResult(getPickImageIntent(), 100)
    }

    private fun getPickImageIntent(): Intent? {
        var chooserIntent: Intent? = null

        var intentList: MutableList<Intent> = ArrayList()

        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri())

        intentList = addIntentsToList(this, intentList, pickIntent)
        intentList = addIntentsToList(this, intentList, takePhotoIntent)

        if (intentList.size > 0) {
            chooserIntent = Intent.createChooser(
                    intentList.removeAt(intentList.size - 1),
                    "Select Image"
            )
            chooserIntent!!.putExtra(
                    Intent.EXTRA_INITIAL_INTENTS,
                    intentList.toTypedArray<Parcelable>()
            )
        }

        return chooserIntent
    }

    private fun addIntentsToList(
            context: Context,
            list: MutableList<Intent>,
            intent: Intent
    ): MutableList<Intent> {
        val resInfo = context.packageManager.queryIntentActivities(intent, 0)
        for (resolveInfo in resInfo) {
            val packageName = resolveInfo.activityInfo.packageName
            val targetedIntent = Intent(intent)
            targetedIntent.setPackage(packageName)
            list.add(targetedIntent)
        }
        return list
    }

    private fun setImageUri(): Uri {
        val folder = File("${getExternalFilesDir(Environment.DIRECTORY_DCIM)}")
        folder.mkdirs()

        val file = File(folder, "Image_Tmp.jpg")
        if (file.exists())
            file.delete()
        file.createNewFile()
        val imageUri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + ".fileProvider",
                file
        )
        imgPath = file.absolutePath
        return imageUri!!
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data?.data != null) {
            val selectedMedia : Uri? = data.data
            Log.e("TAG", "Uri : $selectedMedia")
        val path = data.data?.path
        if (path!!.contains("/video/")) {
            Log.e("TAG", "Video : $path")
        } else if (path.contains("/images/")) {
            Log.e("TAG", "Image : $path")
            image1.setImageURI(selectedMedia)
            getBitmapFromUri(selectedMedia)
        } }else{
            val uri : Uri = Uri.fromFile(File(imgPath ?: ""))
            image1.setImageURI(uri)
        }
    }

    @Throws(IOException::class)
    private fun getBitmapFromUri(uri: Uri?): Bitmap {
        val parcelFileDescriptor: ParcelFileDescriptor = contentResolver.openFileDescriptor(uri!!, "r")!!
        val fileDescriptor: FileDescriptor = parcelFileDescriptor.fileDescriptor
        val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    }
}