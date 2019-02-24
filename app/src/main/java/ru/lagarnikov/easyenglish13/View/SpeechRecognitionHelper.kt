package ru.lagarnikov.easyenglish13.View

import android.widget.Toast
import android.app.Activity
import android.app.AlertDialog
import java.nio.file.Files.size
import android.speech.RecognizerIntent
import android.content.Intent
import android.content.pm.PackageManager
import ru.lagarnikov.easyenglish13.VOICE_RECOGNITION_REQUEST_CODE
import android.content.DialogInterface
import android.net.Uri


class SpeechRecognitionHelper {
    /**
     * Запускает процесс распознавания. Проверяет наличие Activity для распознавания речи.
     * Если Activity нет, отправляет пользователя в маркет установить Голосовой Поиск
     * Google. Если активи для распознавания есть, то отправляет Intent для ее запуска.
     *
     * @param ownerActivity Activity, которая инициировала процесс распознавания
     */
    fun run(ownerActivity: Activity, textNotSpechProgram:String) {
        // проверяем есть ли Activity для распознавания
        if (isSpeechRecognitionActivityPresented(ownerActivity) === true) {
            // если есть - запускаем распознавание
            startRecognitionActivity(ownerActivity)
        } else {
            // если нет, то показываем уведомление что надо установить Голосовой Поиск
            Toast.makeText(
                ownerActivity,
                textNotSpechProgram,
                Toast.LENGTH_LONG
            ).show()
            // начинаем процесс установки
            installGoogleVoiceSearch(ownerActivity)
        }
    }

    /**
     * Проверяет наличие Activity способной выполнить распознавание речи
     *
     * @param ownerActivity Activity, которая запросила проверку
     * @return true - если есть, false - если такой Activity нет
     */
    private fun isSpeechRecognitionActivityPresented(ownerActivity: Activity): Boolean {
        try {
            // получаем экземпляр менеджера пакетов
            val pm = ownerActivity.packageManager
            // получаем список Activity способных обработать запрос на распознавание
            val activities = pm.queryIntentActivities(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0)

            if (activities.size != 0) {    // если список не пустой
                return true                // то умеем распознавать речь
            }
        } catch (e: Exception) {

        }

        return false // не умеем распознавать речь
    }

    /**
     * Отправляет Intent с запросом на распознавание речи
     * @param ownerActivity инициировавшая запрос Activity
     */
    private fun startRecognitionActivity(ownerActivity: Activity) {

        // создаем Intent с действием RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

        // добавляем дополнительные параметры:
        intent.putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            "Голосовой поиск Inforino"
        )    // текстовая подсказка пользователю
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
        )    // модель распознавания оптимальная для распознавания коротких фраз-поисковых запросов
        intent.putExtra(
            RecognizerIntent.EXTRA_MAX_RESULTS,
            1
        )    // количество результатов, которое мы хотим получить, в данном случае хотим только первый - самый релевантный
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

        // стартуем Activity и ждем от нее результата
        ownerActivity.startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE)
    }

    /**
     * Запрашивает разрешение на установку Голосового Поиска Google, отображая диалог. Если разрешение
     * получено - направляет пользователя в маркет.
     * @param ownerActivity Activity инициировавшая установку
     */
    private fun installGoogleVoiceSearch(ownerActivity: Activity) {

        // создаем диалог, который спросит у пользователя хочет ли он
        // установить Голосовой Поиск
        val dialog = AlertDialog.Builder(ownerActivity)
            .setMessage("Для распознавания речи необходимо установить \"Голосовой поиск Google\"")    // сообщение
            .setTitle("Внимание")    // заголовок диалога
            .setPositiveButton("Установить", DialogInterface.OnClickListener { dialog, which ->
                // положительная кнопка

// обработчик нажатия на кнопку Установить
                try {
                    // создаем Intent для открытия в маркете странички с приложением
                    // Голосовой Поиск имя пакета: com.google.android.voicesearch
                    val intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.voicesearch"))
                    // настраиваем флаги, чтобы маркет не попал к в историю нашего приложения (стек Activity)
                    intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
                    // отправляем Intent
                    ownerActivity.startActivity(intent)
                } catch (ex: Exception) {
                    // не удалось открыть маркет
                    // например из-за того что он не установлен
                    // ничего не поделаешь
                }
            })

            .setNegativeButton("Отмена", null)    // негативная кнопка
            .create()

        dialog.show()    // показываем диалог
    }
}