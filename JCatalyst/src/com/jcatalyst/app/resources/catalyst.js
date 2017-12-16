class CatalystElement extends HTMLElement {
	constructor() {
		super();
	}
}

class StatusBar extends CatalystElement {
	constructor() {
		super();
		$(this).click(function () {
			alert("Run custom code!");
		});
	}
}

$(function () {
	window.customElements.define("status-bar", StatusBar);

	$.metaTag = function (name) {
		return $("meta[name=" + name + "]");
	}
	$.meta = function (name) {
		return $.metaTag(name).attr("content");
	}
	$.fn.changeElementType = function (newType) {
		var newElements,
		attrs,
		newElement;
		
		this.each(function () {
			attrs = {};
			
			$.each(this.attributes, function () {
				attrs[this.nodeName] = this.nodeValue;
			});
			
			newElement = $("<" + newType + "/>", attrs).append($(this).contents());
			
			$(this).replaceWith(newElement);
			
			if (!newElements) {
				newElements = newElement;
			} else {
				$.merge(newElements, newElement);
			}
		});
		
		return $(newElements);
	};

	var ws = new WebSocket("ws://" + location.hostname + ":" + location.port + "/events/live/");
	
	ws.onmessage = function (msg) {
		var json = JSON.parse(msg.data);
		if (json.type == "value-change") {
			$("p[catalyst='" + json.name + "']").text(json.value);
			$("input[catalyst='" + json.name + "']").val(json.value);
		} else if (json.type == "load-list") {
			var bce = $("*[catalyst-list='" + json.name + "'");
			for (i in json.value) {
				var sce = bce.clone();
				sce.find("*[catalyst='list-element']").text(json.value[i]);
				bce.parent().append(sce);
			}
			bce.remove();
		}
	};
	ws.onclose = function () {
		location.reload()
	};
	ws.onopen = function () {
		console.log("[CatalystWebSocket] Successfully connected (" + $.meta("catalyst-id") + ") => ws://" + location.hostname + ":" + location.port + "/events/live/");
		$("input[catalyst]").each(function() {
			if ($(this).is("[value]")) {
				modifyValue($(this).attr("catalyst"), $(this).val());
			}
		});
		loadList("samplelist");
	}

	function modifyValue(name, value) {
		console.log("[CatalystWebSocket] Modifying value " + name);
		ws.send(JSON.stringify({
			"type": "value-change",
			"id": $.meta("catalyst-id"),
			"name": name,
			"value": value
		}));
	}

	function loadList(name) {
		console.log("[CatalystWebSocket] Loading list " + name);
		ws.send(JSON.stringify({
			"type": "load-list",
			"id": $.meta("catalyst-id"),
			"name": name
		}));
	}

	$(".dropdown>.label").click(function () {
		$(this.parentElement).toggleClass("show");
	});

	$(".btn.togglable").click(function () {
		$(this).toggleClass("toggled");
	});

	$(document.body).on("input", "input[catalyst]", function(e) {
		modifyValue($(this).attr("catalyst"), $(this).val());
	});

	$(document.body).on("change", ".input-group>input[type='file']", function(e) {
		if (e.target.files != undefined) {
			if (e.target.files.length > 1) {
				$(this.parentElement).find("label").text(e.target.files[0].name + " + " + (e.target.files.length - 1) + " more file(s)");
			} else {
				$(this.parentElement).find("label").text(e.target.files[0].name);
			}
		}
	});
});