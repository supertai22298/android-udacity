package com.example.familyapp.viewmodel.base;

import com.example.familyapp.model.response.BaseResponse;

public interface ResponseHandlerListener<T> {
    void onSuccess(BaseResponse<T> response);
    void onApiFailed(Throwable t);
}
