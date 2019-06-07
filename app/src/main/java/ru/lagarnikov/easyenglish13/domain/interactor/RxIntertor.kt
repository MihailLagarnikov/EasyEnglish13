package ru.lagarnikov.easyenglish13.domain.interactor

import io.reactivex.Single
import ru.lagarnikov.easyenglish13.domain.Repositoriy
import ru.lagarnikov.easyenglish13.domain.UiPresenter
import ru.lagarnikov.easyenglish13.domain.model.DataQuestion
import ru.lagarnikov.easyenglish13.presentation.DomainInterfase
import ru.lagarnikov.easyenglish13.presentation.FragmentName
import java.util.concurrent.Callable

class RxIntertor(val repositoriy: Repositoriy
                 , val uiViewModelTestFragment: UiPresenter.ViewModelTestFragments
                 , val uiViewModelStartScreen: UiPresenter.ViewModelStartScreen):DomainInterfase.RxInteratorTest {
    private val mTestInterator=TestFragmentInteractor(repositoriy
        ,uiViewModelTestFragment
        ,uiViewModelStartScreen)



    override fun startLesson(themeName: String): Single<DataQuestion> {
        val observ= Single.fromCallable(object : Callable<DataQuestion> {
            override fun call(): DataQuestion {
                return mTestInterator.startLesson(themeName)
            }
        })

        return observ
    }

    override fun repitLesson(themeName: String): Single<DataQuestion> {
        val observ= Single.fromCallable(object : Callable<DataQuestion> {
            override fun call(): DataQuestion {
                return mTestInterator.repitLesson(themeName)
            }
        })

        return observ
    }

    override fun cerateNewQuestionData(): Single<DataQuestion> {
        val observ= Single.fromCallable(object : Callable<DataQuestion> {
            override fun call(): DataQuestion {
                return mTestInterator.getNextTesFragment()
            }
        })

        return observ
    }

    override fun pause() {
        mTestInterator.pause()
    }

    override fun resume() {
        mTestInterator.resume()
    }

    override fun destroy() {
        mTestInterator.destroy()
    }

    override fun exitLesson() {
        return mTestInterator.exitLesson()
    }

    override fun answerDone(fragmentName: FragmentName, success: Boolean):Single<Boolean> {

        val observ=Single.fromCallable(object : Callable<Boolean>{
            override fun call(): Boolean {
                return mTestInterator.answerDone(fragmentName,success)
            }
        })
        return observ
    }
}