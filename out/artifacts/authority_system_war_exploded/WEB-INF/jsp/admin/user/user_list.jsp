<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户列表</title>
<script type="text/javascript" src="${ctx}/static/js/hp_list.js"></script>
</head>
<body>
	<form>
		<div class="demoTable">
			用户名：
			<div class="layui-inline">
				<input class="layui-input" name="userName" id="userName"
					autocomplete="off">
			</div>
			<button class="layui-btn bt_search" data-type="reload">搜索</button>
			<button type="reset" class="layui-btn layui-btn-primary">重置</button>
		</div>
	</form>
	<div style="height: 10px;" />
	<div>
			<button class="layui-btn bt_add" data="893px, 550px" data-url="user/user_add.jsp"><span class='iconfont icon-add'></span>&nbsp;新增</button>
			<button class="layui-btn layui-btn-warm bt_update" data="893px, 550px" data-url="user/user_update.jsp"><span class='iconfont icon-brush'></span>&nbsp;修改</button>
			<button class="layui-btn layui-btn-danger bt_delete" data-url="${ctx}/a/user/delete"><span class='iconfont icon-delete'></span>&nbsp;删除</button>
			<button class="layui-btn layui-btn-normal bt_setRole" data="893px, 550px" data-url="user/user_setRole.jsp"><span class='iconfont icon-group'></span>&nbsp;分配角色</button>
	</div>

	<table class="layui-hide" id="user" lay-data="{id: 'user'}"></table>
	<script>
		layui.use('table', function() {
			var table = layui.table;

			table.render({
				elem : '#user',
				<%--url : '${ctx}/a/user/list',--%>
				cellMinWidth : 80,
				page : true,
				cols : [ [ {
					type : 'checkbox'
				}, {
					field : 'id',
					width : 300,
					title : 'ID',
					sort : true
				}, {
					field : 'userName',
					title : '用户名'
				}, {
					field : 'nickname',
					title : '昵称'
				}, {
					field : 'tel',
					title : '电话'
				}, {
					field : 'sex',
					title : '性别',
					width : 60,
					templet : function (data){
						if (data.sex=='1') {
							return '男';
						}else {
							return '女';
						}
					}
				}, {
					field : 'email',
					title : '邮箱'
				}, {
					field : 'status',
					title : '状态',
					width : 60,
					templet : function (data){
						if (data.status=='on') {
							return '启用';
						}else {
							return '禁用';
						}
					}
				}, {
					field : 'userImg',
					title : '头像'
				}, {
					field : 'createTime',
					title : '创建时间'
				}, {
					field : 'updateTime',
					title : '更新时间'
				} ] ],
				data: [
					{"id":"1026027802194242a4adb367b2780e46","userName":"tom","nickname":"石头","tel":"13717594450","sex":"男",
						"email":"2873444@qq.com","status":"on","userImg":"","createTime":"2019-01-17 12:48:46","updateTime":""},
					{"id":"1026027802194242a4adb367b2780e41","userName":"tom","nickname":"石头2","tel":"13717594450","sex":"男",
						"email":"2873444@qq.com","status":"on","userImg":"","createTime":"2019-01-17 12:48:46","updateTime":""},
					{"id":"1026027802194242a4adb367b2780e3","userName":"tom","nickname":"石头3","tel":"13717594450","sex":"男",
						"email":"2873444@qq.com","status":"启用","userImg":"","createTime":"2019-01-17 12:48:46","updateTime":""},
					{"id":"1026027802194242a4adb367b2780e43","userName":"tom","nickname":"石头4","tel":"13717594450","sex":"男",
						"email":"2873444@qq.com","status":"启用","userImg":"","createTime":"2019-01-17 12:48:46","updateTime":""}
				]

			});

			//搜索条件
			var $ = layui.$, active = {
				reload : function() {
					table.reload($('table').attr("id"), {
						where : {
							userName : $('#userName').val()
						}
					});
				}
			};


			//触发搜索条件事件
			$('.bt_search').on('click', function (e){
				var type = $(this).data('type');
				active[type] ? active[type].call(this) : '';
				return false;
			})

		});
	</script>
</body>
</html>
