package com.youlingcao.annotation.api;

public interface ViewBinder<T> {
    void bindView(T host, Object object, ViewFinder flipper);

    void unBindView(T host);
}
