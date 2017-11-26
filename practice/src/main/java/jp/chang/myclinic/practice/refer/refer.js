"use strict";

var Compiler = require("myclinic-drawer").Compiler;
var Box = require("myclinic-drawer").Box;

function Refer(data){
	this.compiler = new Compiler();
	this.pointDict = {};
	this.boxDict = {};
	this.setup();
	if( data ){
		this.setData(data);
	}
}

Refer.prototype.setup = function(){
	var compiler = this.compiler;
	var page = Box.createA4Box();
	compiler.createFont("serif-6", "MS Mincho", 6);
	compiler.createFont("serif-5", "MS Mincho", 5);
	compiler.createFont("serif-5-bold", "MS Mincho", 5, "bold");
	compiler.createFont("serif-4", "MS Mincho", 4);
	compiler.setPoint("title", page.cx(), 41);
	compiler.setPoint("referHospital", 30, 58);
	compiler.setPoint("referDoctor", 30, 58+6);
	compiler.setPoint("patientName", 30, 80);
	compiler.setPoint("patientInfo", 50, 90);
	compiler.setPoint("diagnosis", 30, 102);
	compiler.setPoint("issueDate", 30, 220);
	compiler.setPoint("address", 118, 220);
	compiler.setBox("content", new Box(30, 115, 170, 210));
};

Refer.prototype.setTitle = function(title){
	var c = this.compiler,
		p = c.getPoint("title");
	c.setFont("serif-5-bold");
	c.textAt(title, p.x, p.y, "center", "center", {extraSpace: 5});

};
Refer.prototype.setReferHospital = function(name){
	var c = this.compiler,
		p = c.getPoint("referHospital");
	c.setFont("serif-4");
	c.textAt(name, p.x, p.y, "left", "bottom");
};

Refer.prototype.setReferDoctor = function(text){
	var c = this.compiler,
		p = c.getPoint("referDoctor");
	c.setFont("serif-4");
	c.textAt(text, p.x, p.y, "left", "bottom");	
};

Refer.prototype.setPatientName = function(name){
	var c = this.compiler,
		p = c.getPoint("patientName");
	c.setFont("serif-5");
	c.textAt(name, p.x, p.y, "left", "bottom");	
};

Refer.prototype.setPatientInfo = function(text){
	var c = this.compiler,
		p = c.getPoint("patientInfo");
	c.setFont("serif-4");
	c.textAt(text, p.x, p.y, "left", "bottom");	
};

Refer.prototype.setDiagnosis = function(text){
	var c = this.compiler,
		p = c.getPoint("diagnosis");
	c.setFont("serif-5");
	c.textAt(text, p.x, p.y, "left", "bottom");	
};

Refer.prototype.setIssueDate = function(text){
	var c = this.compiler,
		p = c.getPoint("issueDate");
	c.setFont("serif-4");
	c.textAt(text, p.x, p.y, "left", "bottom");	
};

Refer.prototype.setAddress = function(addr1, addr2, addr3, addr4, clinicName, doctorName){
	var c = this.compiler,
		p = c.getPoint("address");
	c.setFont("serif-4");
	var x = p.x, y = p.y + 4;
	var lineHeight = 4 + 2;
	c.textAt(addr1, x, y, "left", "bottom");
	y += lineHeight;
	c.textAt(addr2, x, y, "left", "bottom");
	y += lineHeight;
	c.textAt(addr3, x, y, "left", "bottom");
	y += lineHeight;
	c.textAt(addr4, x, y, "left", "bottom");
	y += lineHeight;
	y += 4;
	c.textAt(clinicName, x, y, "left", "bottom");
	y += lineHeight;
	var txt = "院長";
	var mes = c.measureText(txt);
	c.textAt(txt, x, y, "left", "center");
	x += mes.cx + 4;
	c.setFont("serif-6");
	mes = c.measureText(doctorName);
	c.textAt(doctorName, x, y, "left", "center");
	x += mes.cx + 8;
	c.setFont("serif-4");
	c.textAt("㊞", x, y, "left", "center");
};

Refer.prototype.setContent = function(content){
	var c = this.compiler,
		box = c.getBox("content");
	var contentLines = content.split(/\r\n|\r|\n/g);
	c.setFont("serif-4");
	var lines = contentLines.reduce(function(cur, line){
		return cur.concat(c.breakLines(line, box.width()));
	}.bind(this), []);
	var leading = 0.8;
	c.multilineText(lines, box, "left", "top", leading);
	// var x = box.left(), y = box.top();
	// var fontInfo = c.getCurrentFontInfo(), leading = 0;
	// lines.forEach(function(line){
	// 	c.textAt(line, x, y, "left", "top");
	// 	y += fontInfo.fontSize + leading;
	// });
};

Refer.prototype.setData = function(data){
	if( "title" in data ){
		this.setTitle(data.title);
	}
	if( "referHospital" in data ){
		this.setReferHospital(data.referHospital);
	}
	if( "referDoctor" in data ){
		this.setReferDoctor(data.referDoctor);
	}
	if( "patientName" in data ){
		this.setPatientName(data.patientName);
	}
	if( "patientInfo" in data ){
		this.setPatientInfo(data.patientInfo);
	}
	if( "diagnosis" in data ){
		this.setDiagnosis(data.diagnosis);
	}
	if( "issueDate" in data ){
		this.setIssueDate(data.issueDate);
	}
	if( "address" in data ){
		this.setAddress.apply(this, data.address);
	}
	if( "content" in data ){
		this.setContent(data.content);
	}
};

Refer.prototype.getOps = function(){
	return this.compiler.getOps();
};

module.exports = Refer;
