function print() {
  var text = document.getElementsByTagName('input')[0].value
  const message = document.querySelector('#target')
  message.textContent = text
}