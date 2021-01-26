package doa.ai.main.writeIdeation

import doa.ai.main.notes.ideation.IdeationView
import doa.ai.main.notes.ideation.model.BodyIdea
import doa.ai.main.notes.ideation.model.BodyIdeaDelete
import doa.ai.main.notes.ideation.model.ResponseIdea

import doa.ai.network.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class WriteIdeationPresenter  {

    private  var view: WriteIdeationView? = null
    private lateinit var service: Service
    private var compositDisposible: CompositeDisposable? = null

    fun ideationContract() {
        service = Service.Create.service()
    }

    fun attachView(view: WriteIdeationView) {
        this.view = view
        compositDisposible = CompositeDisposable()
    }

    fun detachView() {
        if (compositDisposible != null && !compositDisposible?.isDisposed!!)
            compositDisposible?.dispose()

    }

    fun postIdeation( token : String, desc : String,title : String,labels : List<String>){
        val bodyIdea = BodyIdea(desc, title, labels)
        val postIdea = service.setPostIdea(token,bodyIdea)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe{
                    view?.showLoad()
                }
                .subscribe (
                        {
                            it.result?.let { it1 -> view?.showSucces(it1) }
                            view?.hideLoad()
                            handleSuccess(it)

                        }, this::handleError
                )
        compositDisposible?.add(postIdea)

    }


    private fun handleSuccess (responseIdea: ResponseIdea) {
        view?.hideLoad()



    }

    private fun handleError(t: Throwable){
        view?.hideLoad()
        t.message?.let { view?.message("Gagal masukan data, error : "+it) }


    }
}