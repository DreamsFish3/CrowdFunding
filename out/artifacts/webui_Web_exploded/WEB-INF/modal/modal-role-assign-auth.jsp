<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="assignModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div  class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">众筹网系统弹窗</h4>
            </div>
            <div class="modal-body">
                <%--这个ul标签是zTree动态生成的节点所依附的静态节点--%>
                <ul id="authTreeDemo" class="ztree"></ul>
            </div>
            <div class="modal-footer">
                <button id="assignBtn" type="button" class="btn btn-primary">保存</button>
            </div>
        </div>
    </div>
</div>
