package com.example.familyapp.viewmodel.base;

import com.example.familyapp.model.response.BaseResponse;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class ResponseHandler<T>  {
    private BaseViewModel viewModel;
    public ResponseHandler(BaseViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Config request an API with the listener for success and failed
     * @param request
     * @param listener
     */
    public void setRequest(Single<BaseResponse<T>> request, final ResponseHandlerListener<T> listener) {
         request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        /* Notify that view should hide loading dialog */
                        viewModel.isLoading.postValue(false);
                    }
                })
                .subscribe(new SingleObserver<BaseResponse<T>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        /* Notify that view should show loading dialog */
                        viewModel.isLoading.postValue(true);
                        /* RxJava register that listener will be managed by Disposable
                        * We can clear stop or continue listener the response from server*/
                        viewModel.addDisposable(d);
                    }

                    @Override
                    public void onSuccess(BaseResponse<T> response) {
                        /* We received response data from server here
                        * After that send response back to the class, which is the owner request*/
                        if (listener != null) {
                            listener.onSuccess(response);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        /* Failed API will be return here (400, 404, 401, no connection)
                         * After that send exception back to the class, which is the owner request*/
                        if (listener != null) {
                            listener.onApiFailed(e);
                        }
                    }
                });
    }

    public void setBackgroundRequest(Single<BaseResponse<T>> request, final ResponseHandlerListener<T> listener) {
        request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                })
                .subscribe(new SingleObserver<BaseResponse<T>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        viewModel.addDisposable(d);
                    }

                    @Override
                    public void onSuccess(BaseResponse<T> response) {
                        listener.onSuccess(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }
}
