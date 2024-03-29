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
			电话：
			<div class="layui-inline">
				<input class="layui-input" name="tel" id="tel"
					   autocomplete="off">
			</div>
			邮箱：
			<div class="layui-inline">
				<input class="layui-input" name="email" id="email"
					   autocomplete="off">
			</div>
			<button class="layui-btn bt_search" data-type="reload">搜索</button>
			<button type="reset" class="layui-btn layui-btn-primary">重置</button>
		</div>
	</form>
	<div style="height: 10px;" />
    <div>
        <button class="layui-btn bt_add" data="893px, 550px" data-url="${ctx}/user/toAddPage"><span class='iconfont icon-add'></span>&nbsp;新增</button>
        <button class="layui-btn layui-btn-warm bt_update" data="893px, 550px" data-url="${ctx}/user/toUpdatePage"><span class='iconfont icon-brush'></span>&nbsp;修改</button>
        <button class="layui-btn layui-btn-danger bt_delete" data-url="${ctx}/user/delete"><span class='iconfont icon-delete'></span>&nbsp;删除</button>
        <button class="layui-btn layui-btn-normal bt_setRole" data="893px, 550px" data-url="user/toSetRole"><span class='iconfont icon-group'></span>&nbsp;分配角色</button>
    </div>

	<table class="layui-hide" id="user" lay-data="{id: 'user'}"></table>
	<script>
		layui.use('table', function() {
			var table = layui.table;
			var util = layui.util;

			table.render({
				elem:'#user'
				,height:312
				,url:'${ctx}/user/list'
				,parseData:function (res) {
					return{
						"code":0,
						"msg":res.message,
						"count":res.total,
						"data":res.records
					};
				}
				,method:'post'
				,page: true
				,cols:[[
					{field:'id',type:'checkbox',width:80,fixed:'center'}
					,{field:'id',title:'ID',width:300,sort:true,fixed:'center'}
					,{field:'userName',title:'用户名',width:80}
					,{field:'nickName',title:'昵称',width:80}
					,{field:'tel',title:'电话',width:120}
					,{field:'sex',title:'性别',width:80,
					templet:function (res) {
						if (res.sex==='1'){
							return '男';
						} else {
							return '女';
						}
					}}
					,{field:'email',title:'邮箱',width:150}
					,{field:'status',title:'状态',width:65,
					templet:function (res) {
						if (res.status === 'on'){
							return '启用';
						} else {
							return '禁用';
						}
					}}
					,{field:'userImg',title:'头像',width:100}
					,{field:'createTime',title:'创建时间',width:120,
					templet:function (res) {
						if (res.createTime !== null){
							return util.toDateString(res.createTime,"yyyy-MM-dd HH:mm:ss");
						} else {
							return "";
						}
					}}
					,{field:'updateTime',title:'更新时间',width:120,
					templet:function (res) {
						if(res.updateTime !== null){
							return util.toDateString(res.updateTime,"yyyy-MM-dd HH:mm:ss");
						}else{
							return "";
						}
					}}
				]]
			});

			//搜索条件
			var $ = layui.$, active = {
				reload : function() {
					table.reload($('table').attr("id"), {
						where : {
							userName : $('#userName').val(),
							tel : $('#tel').val(),
							email : $('#email').val()
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
