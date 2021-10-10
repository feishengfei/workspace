#!/usr/bin/env python3

""" Form layout example. """

import sys

from PyQt5.QtWidgets import QApplication
from PyQt5.QtWidgets import QFormLayout
from PyQt5.QtWidgets import QLineEdit
from PyQt5.QtWidgets import QWidget

def main():
  app = QApplication(sys.argv)
  window = QWidget()
  window.setWindowTitle('QFormLayout')
  layout = QFormLayout()
  layout.addRow('Name:', QLineEdit())
  layout.addRow('Age:', QLineEdit())
  layout.addRow('Job:', QLineEdit())
  layout.addRow('Hobbies:', QLineEdit())
  window.setLayout(layout)
  window.show()
  sys.exit(app.exec_())


if __name__ == '__main__':
    main()
