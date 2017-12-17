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


function getObjectValueFromString(foo, query) {
	return query.split('.')
	.reduce(function (object, property) {
  
	  return object[property];
	}, foo);
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

	/*$.fn.data = function (v) {
		return $(this).attr("data-" + v);
	};

	$.fn.data = function (v, nv) {
		$(this).attr("data
		return $(this).attr("data-" + v);
	};*/

	$.fn.hasData = function (v) {
		return $(this).data(v) != undefined && $(this).data(v) != null;
	}

	$("*[data-catalyst-list]").addClass("hide");

	var ws = new WebSocket("ws://" + location.hostname + ":" + location.port + "/events/live/");

	ws.onmessage = function (msg) {
		var json = JSON.parse(msg.data);

		if (json.type == "value-change") {
			$("p[data-catalyst='" + json.name + "'], span[data-catalyst='" + json.name + "'], a[data-catalyst='" + json.name + "']").text(json.value);
			$("input[data-catalyst='" + json.name + "']").val(json.value);
		} else if (json.type == "load-list") {
			//alert(json.name);
			//var bce = $("*[data-catalyst-list=\"" + json.name + "\"").first();
			var bce;
			$("*").each(function() {
				console.log(json.name);
				if ($(this).data("catalyst-list") == json.name) {
					bce = $(this);
					//alert(bce);
				}
			});
			//console.log(bce);
			var badds = [];
			bce.parent().children().each(function () {
				if (this != bce[0] && !($(this).hasData("no-catalyst"))) {
					$(this).remove();
				} else {
					if ($(this).data("catalyst-list-position") == "bottom") {
						badds.push($(this));
					}
				}
			});
			for (i in json.value) {
				var sce = bce.clone();
				sce.find("*[data-catalyst='list-element']").text(json.value[i]);
				sce.find("*[data-catalyst-query]").each(function () {
					$(this).text(getObjectValueFromString(json.value[i], $(this).data("catalyst-query")));
				});
				sce.removeClass("hide");
				bce.addClass("hide");
				bce.parent().append(sce);
			}
			for (i in badds) {
				var b = badds[i];
				b.appendTo(bce.parent());
			}
		}
	};
	ws.onclose = function () {
		//console.log("Closed");
		//location.reload();
	};
	a = function () {
		var log = console.log;
		console.log = function () {
			logWireless("log", arguments);
			log.apply(this, Array.prototype.slice.call(arguments));
		};

		var error = console.error;
		console.error = function () {
			logWireless("err", arguments);
			error.apply(this, Array.prototype.slice.call(arguments));
		};
	};
	ws.onopen = function () {
		console.log("[CatalystWebSocket] Successfully connected (" + $.meta("data-catalyst-id") + ") => ws://" + location.hostname + ":" + location.port + "/events/live/");
		$("input[data-catalyst]").each(function() {
			if ($(this).is("[value]")) {
				modifyValue($(this).data("catalyst"), $(this).val());
			}
		});
		//loadList("todolist");
		a();
	}

	function logWireless(c, a) {
		ws.send(JSON.stringify({
			"type": "console",
			"id": $.meta("catalyst-id"),
			"name": c,
			"value": Array.prototype.slice.call(a)
		}));
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

	function submitValue(name, value) {
		console.log("[CatalystWebSocket] Submitting value " + name);
		ws.send(JSON.stringify({
			"type": "submit",
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

	$(document.body).on("input", "input[data-catalyst]", function(e) {
		modifyValue($(this).data("catalyst"), $(this).val());
	});

	$(document.body).on("keypress", "input[data-catalyst]", function(e) {
		if (e.which == 13) {
			submitValue($(this).data("catalyst"), $(this).val());
		}
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