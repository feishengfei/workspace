#!/usr/bin/env python3

"""Signals and slots example. """

import sys
import functools

from PyQt5.QtWidgets import QApplication
from PyQt5.QtWidgets import QLabel
from PyQt5.QtWidgets import QPushButton
from PyQt5.QtWidgets import QVBoxLayout
from PyQt5.QtWidgets import QWidget


def greeting(w):
    """Slot function."""
    if w.text():
        w.setText("")
    else:
        w.setText("Hello World!")


def main():
  app = QApplication(sys.argv)
  window = QWidget()
  window.setWindowTitle('Signals and slots')
  layout = QVBoxLayout()

  msg = QLabel('')

  btn = QPushButton('Greet')
  btn.clicked.connect(functools.partial(greeting, msg))

  layout.addWidget(btn)
  layout.addWidget(msg)
  window.setLayout(layout)
  window.show()
  sys.exit(app.exec_())


if __name__ == '__main__':
    main()
