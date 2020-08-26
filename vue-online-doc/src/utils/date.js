function timeago(timestamp) { //将时间转化成离现在几分钟，几小时的格式

  var date = new Date(timestamp);
  var diffMs = Date.now() - timestamp;//相差的毫秒数
  var diff = (diffMs) / 1000 / 60 / 60;  //相差的时间 小时数
  // alert(date.toLocaleString())
  var year = date.getFullYear();
  var month = date.getMonth() + 1;
  var day = date.getDate();
  var hours = date.getHours();
  if (hours < 10)
    hours = '0' + hours;
  var minutes = date.getMinutes();
  if (minutes < 10)
    minutes = '0' + minutes;
  var seconds = date.getSeconds();
  if (seconds < 10)
    seconds = '0' + seconds;

  return year + '.' + month + '.' + day + ' ' + hours + ':' + minutes + ':' + seconds;
}

export default {
  timeago
}
