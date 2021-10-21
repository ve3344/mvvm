package me.lwb.mvvm.exception


enum class RequestErrorCode(val code: Int, val message: String, val fatal: Boolean = false) {
    NETWORK_ERROR(100001, "网络错误", false),
    TIMEOUT_ERROR(100002, "连接超时", false),
    UNKNOWN(200001, "未知错误", true),
    PARSE_ERROR(200002, "解析错误", true),
    SSL_ERROR(200003, "证书出错", true),
    HTTP_OTHER_ERROR(200004, "HTTP网络错误", true),
    HTTP_SERVER_ERROR(200005, "HTTP服务器异常", true),
    HTTP_CLIENT_ERROR(200006, "HTTP请求异常", true)
    ;

}