var table;

function searchFrom() {
    table.draw(true);
}

function clearFrom(form) {
    var array = new Array();
    array.push('input[type=text]');
    array.push('select');
    var str = array.join();
    $('#' + form).find(str).each(function () {
        $(this).val('');
    });

    searchFrom();
}

var appDialog = {

    //网络错误时提示延迟时间
    errorTimeout: 500,

    /**
     * 消息提示： 显示消息
     */
    info: function (content) {
        swal({
            title: '提示',
            text: content,
            type: 'info'
        });
    },

    /**
     * 显示操作结果，并在2秒后调用link(redirect) || redirect()
     */
    success: function (content, redirect, title) {
        swal({
            title: title || '提示',
            text: content,
            type: 'success',
            timer: 2000
        }).then(
            function () {
                if (redirect) {
                    if (typeof redirect == 'function') {
                        redirect();
                    } else {
                        link(redirect);
                    }
                }
            },
            function (dismiss) {
            }
        );
    },

    /**
     * 警告提示： 显示消息
     */
    warning: function (content, callback) {
        swal({
            title: '提示',
            text: content,
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(function (isConfirm) {
            if (isConfirm.value === true) {
                if (callback) {
                    if (typeof callback == 'function') {
                        callback();
                    }
                }
            }
        });
    },

    /**
     * 错误提示： 显示消息
     */
    error: function (content) {
        swal({
            title: '提示',
            text: content,
            type: 'error'
        });
    },

    /**
     * 显示提交确认信息，点击确认后请求action，结果返回code=200时跳调用link(redirect)
     */
    comfirm: function (content, action, redirect, callback) {
        appDialog.warning(content, function () {
            appDialog.sendRequest(action, '', function (result) {
                if (result && result.code == 200) {
                    if (redirect) {
                        if (typeof redirect == 'function') {
                            redirect();
                        } else {
                            link(redirect);
                        }
                    }
                    appDialog.success("操作成功");
                } else {
                    appDialog.error(result.msg);
                }

                callback && callback(result && result.code == 200);
            });
        });
    },

    /**
     * 提交表单并显示提交结果，结果返回code=200时跳调用link(redirect)
     */
    submit_form: function (sucMsg, errMsg, formId, redirect) {
        var l = $("#" + formId + "Btn").ladda();
        var form = $("#" + formId);
        var url = form.attr("action");
        var params = form.formSerialize();

        if (!url) {
            throw new Error("need url!");
        }

        l.ladda('start');

        appDialog.sendRequest(url, params, function (result) {
            l.ladda('stop');
            if (result && result.code == 200) {
                appDialog.success(result.msg || sucMsg, redirect);
            } else {
                appDialog.error(result.msg || errMsg);
            }
        });
    },

    /**
     * 发送请求
     */
    sendRequest: function (action, params, callback) {
        $.ajax({
            type: "POST",
            url: action,
            data: params,
            dataType: 'json',
            error: function (xhr) {
                setTimeout(function () {
                    if (xhr.statusText == "parsererror" && xhr.responseText == "loginOut") {
                        callback({code: -1, msg: "您太长时间未操作或尚未登陆，请重新登陆!"});
                    } else {
                        callback({code: -1, msg: "操作失败!"});
                    }
                }, appDialog.errorTimeout);
            },
            success: callback
        });
    },

    /**
     * 模态框
     */
    dialog: function (title, action, callback) {
        var htmlobj = $.ajax({url: action, async: false});
        bootbox.dialog({
            message: htmlobj.responseText,
            title: title,
            buttons: {
                "success": {
                    label: '保存',
                    className: "btn-primary",
                    callback: callback
                },
                "error": {
                    label: '取消',
                    className: "btn-white",
                    callback: function () {}
                }
            }
        });
    }
}

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}