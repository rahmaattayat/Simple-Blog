function formatDate(dateValue) {
	if (!dateValue) {
		return "-";
	}

	var date = new Date(dateValue);

	var day = String(date.getDate()).padStart(2, '0');
	var monthNames = ["Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des"];
	var month = monthNames[date.getMonth()];
	var year = date.getFullYear();
	var hour = String(date.getHours()).padStart(2, '0');
	var minute = String(date.getMinutes()).padStart(2, '0');

	return day + ' ' + month + ' ' + year + ', ' + hour + ':' + minute;
}

function showErrorMessage(err, defaultMessage) {
	if (err.responseJSON && err.responseJSON.message) {
		alert(err.responseJSON.message);
	} else {
		alert(defaultMessage);
	}
}

$(document).ready(function(){
	var postId = $('#detail_post_id').attr("value");
	console.log("postId - " + postId);
	
	$.ajax({
		url: "/post?id=" + postId
	}).then(function(data) {
		console.log(data);
		$('#detail_title').text(data.title);
		$('#detail_user').text(data.user);
		$('#detail_date').text(formatDate(data.updtDate));
		$('#detail_content').text(data.content);
	}, function(err) {
		console.log(err.responseJSON);
	});
	
	$.ajax({
		url: "/comments?post_id=" + postId
	}).then(function(data) {
		$.each(data, function(index, e) {
			var commentWrapper = $('<div>').addClass('media mb-4');
			var commentBody = $('<div>').addClass('media-body');
			var commentUser = $('<h5>').addClass('mt-0').text(e.user);
			var commentText = $('<div>').text(e.comment);

			commentBody.append(commentUser);
			commentBody.append(commentText);
			commentWrapper.append(commentBody);

			$('#comments').append(commentWrapper);
		});
		console.log(data);
	}, function(err) {
		console.log(err.responseJSON);
	});
	
	$('#detail_delete_btn').click(function(){
		var postId = $('#detail_post_id').attr("value");
		console.log("delete button click! - " + postId);

		$.ajax({
			url: "/post?id=" + postId,
			method: "DELETE"
		}).then(function(data) {
			window.location.href = '/';
		}, function(err) {
			showErrorMessage(err, "Terjadi kesalahan saat menghapus post");
		});
	});
	
	$('#modify_post_btn').click(function(){
		var postId = $('#detail_post_id').attr("value");
		var title = $('#modify_title_text').val();
		var content = $('#modify_content_text').val();
		
		console.log(postId);
		console.log(title);
		console.log(content);
		
		var param = {
			id: postId,
			title: title,
			content: content
		};
		
		$.ajax({
			url: "/post",
			method: "PUT",
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify(param)
		}).then(function(data) {
			window.location.href = '/page/detail/' + postId;
		}, function(err) {
			showErrorMessage(err, "Terjadi kesalahan saat mengubah post");
		});
	});
	
	$('#create_comment_btn').click(function(){
		var postId = $('#detail_post_id').attr("value");
		var user = $('#comment_user_text').val();
		var comment = $('#comment_text').val();
		
		console.log(postId);
		console.log(user);
		console.log(comment);
		
		var param = {
			postId: postId,
			user: user,
			comment: comment
		};
		
		$.ajax({
			url: "/comment",
			method: "POST",
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify(param)
		}).then(function(data) {
			window.location.href = '/page/detail/' + postId;
		}, function(err) {
			showErrorMessage(err, "Username dan komentar wajib diisi");
		});
	});
});