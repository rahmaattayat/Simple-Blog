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

$(document).ready(function(){
	$.ajax({
		url: "/posts"
	}).then(function(data) {
		$.each(data, function(index, e) {
			var card = $('<div>').addClass('card mb-4');
			var cardBody = $('<div>').addClass('card-body');
			var title = $('<h2>').addClass('card-title').text(e.title);
			var content = $('<p>').addClass('card-text').text(e.content);
			var readMore = $('<a>')
				.attr('href', '/page/detail/' + e.id)
				.addClass('btn btn-primary')
				.html('Read More &rarr;');
			var footer = $('<div>')
				.addClass('card-footer text-muted')
				.text('Posted on ' + formatDate(e.updtDate) + ' by ' + e.user);

			cardBody.append(title);
			cardBody.append(content);
			cardBody.append(readMore);
			card.append(cardBody);
			card.append(footer);

			$('#posts').append(card);
		});
		console.log(data);
	}, function(err) {
		if (err.responseJSON && err.responseJSON.message) {
			alert(err.responseJSON.message);
		} else {
			alert("Terjadi kesalahan saat menyimpan post");
		}
	});
	
	$('#save_post_btn').click(function(){
		var user = $('#create_user_text').val();
		var title = $('#create_title_text').val();
		var content = $('#create_content_text').val();
		
		console.log(user);
		console.log(title);
		console.log(content);
		
		var param = {
			user: user,
			title: title,
			content: content
		};
		
		$.ajax({
			url: "/post",
			method: "POST",
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify(param)
		}).then(function(data) {
			window.location.href = '/';
		}, function(err) {
			if (err.responseJSON && err.responseJSON.message) {
				alert(err.responseJSON.message);
			} else {
				alert("Terjadi kesalahan saat menyimpan post");
			}
		});
	});
});