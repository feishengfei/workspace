#include <stdio.h>
#include <ctype.h>
#include <errno.h>
#include <time.h>
#include <string.h>
#include <stdarg.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/stat.h>

#include "rbglobal.h"

#if defined(LOG_LEVEL_DEBUG)
  #define __LOG_LEVEL__ 3
#elif defined(LOG_LEVEL_WARNING)
  #define __LOG_LEVEL__ 2
#elif defined(LOG_LEVEL_FATAL)
  #define __LOG_LEVEL__ 1
#else
  #define __LOG_LEVEL__ 0
#endif

#define RB_BUFFER_LENGTH (1024 * 4) /* internal buffer length */

void RB_DEBUG(const char *msg, ...)
{
#if !defined(NDEBUG) || __LOG_LEVEL__ == 3
  char buf[RB_BUFFER_LENGTH];
  va_list ap;
  va_start(ap, msg); 
  vsnprintf(buf, RB_BUFFER_LENGTH - 1, msg, ap);
  va_end(ap);
  #if !defined(NDEBUG)
  fprintf(stderr, "%s\n", buf); /* add newline */
  #endif
  #if __LOG_LEVEL__ == 3
  RB_LOG("%s", buf);
  #endif
#endif
}

void RB_WARNING(const char *msg, ...)
{
  char buf[RB_BUFFER_LENGTH];
  va_list ap;
  va_start(ap, msg); 
  vsnprintf(buf, RB_BUFFER_LENGTH - 1, msg, ap);
  va_end(ap);
  fprintf(stderr, "\033[33;47m[WARNING]\033[0m%c%s\n", ' ', buf); /* add newline */
#if __LOG_LEVEL__ >= 2
  RB_LOG("[WARNING] %s", buf);
#endif
}

void RB_FATAL(const char *msg, ...)
{
  char buf[RB_BUFFER_LENGTH];
  va_list ap;
  va_start(ap, msg); 
  vsnprintf(buf, RB_BUFFER_LENGTH - 1, msg, ap);
  va_end(ap);
  fprintf(stderr, "\033[31;47m[FATAL]\033[0m%c%s\n", ' ', buf); /* add newline */
#if __LOG_LEVEL__ >= 1
  RB_LOG("[FATAL] %s", buf);
#endif
  exit(1); /* goodbye cruel world */
}

#ifdef arm
#define LOG_HOME "/mnt/disk/"
#else
#define LOG_HOME "/tmp/"
#endif

static FILE *__log_fp = NULL;
static int __log_file_ok = 1;

struct log_conf
{
  int _month;
  int _day;
  int _num;
};

static char *__get_app_name()
{
  char path[256];
  static char name[64];
  char *ret, *p;
  FILE *fp;
  size_t br;

  sprintf(path, "/proc/%d/cmdline", getpid());
  fp = fopen(path, "r");
  br = fread(name, 1, 31, fp);
  name[br] = 0;

  ret = name;
  while (*ret != 0 && !isalpha(*ret))
    ++ret;

  p = ret;
  while (*p != 0 && (isalpha(*p) || *p == '_'))
    ++p;
  *p = 0;

  return ret;
}

static int __open_log()
{
  char path[256];
  char conf[256], filename[32];
  struct log_conf logConf;
  FILE *conf_fp = NULL;
  
  strcpy(path, LOG_HOME);
  strcat(path, __get_app_name());
  strcat(path, "_log/");

  time_t ltime;
  time(&ltime);
  struct tm *t = localtime(&ltime);
  logConf._month = t->tm_mon + 1;
  logConf._day = t->tm_mday;
  logConf._num = 0;

  strcpy(conf, path);
  strcat(conf, "config");

  /* create log dir if necessary */
  if (access(path, F_OK) != 0) {
    if (mkdir(path, 0777) != 0) {
      RB_WARNING("make log dir failed: %s", strerror(errno));
      return -1;
    }
  }
  /* create log configuration file if necessary */
  if (access(conf, F_OK) != 0) {
    conf_fp = fopen(conf, "w");
    if (conf_fp == NULL) {
      RB_WARNING("create log configuration failed: %s", strerror(errno));
      return -1;
    }
    fwrite(&logConf, sizeof(struct log_conf), 1, conf_fp);
    fclose(conf_fp);
  }
  /* read config */
  conf_fp = fopen(conf, "r+");
  if (conf_fp == NULL) {
    RB_WARNING("read log configuration failed: %s", strerror(errno));
    return -1;
  }
  fread(&logConf, sizeof(struct log_conf), 1, conf_fp);
  if (logConf._month != t->tm_mon + 1 || logConf._day != t->tm_mday) {
    logConf._month = t->tm_mon + 1;
    logConf._day = t->tm_mday;
    logConf._num = 0;
  }
  /* create log file */
  sprintf(filename, "log_%02d%02d_%02d.txt", logConf._month, logConf._day, logConf._num);
  strcat(path, filename);
  __log_fp = fopen(path, "w");
  if (__log_fp == NULL) {
    RB_WARNING("write log configuration failed: %s", strerror(errno));
    return -1;
  }
  /* write back config */
  ++logConf._num;
  rewind(conf_fp);
  fwrite(&logConf, sizeof(struct log_conf), 1, conf_fp);
  fclose(conf_fp);

  return 0;
}

static void __close_log()
{
  fclose(__log_fp);
  __log_fp = NULL;
}

void RB_LOG(const char *msg, ...)
{
  if (__log_fp == NULL && __log_file_ok)
    if (__open_log() != 0) {
      __log_file_ok = 0;
      return;
    }

  char buf[RB_BUFFER_LENGTH];
  va_list ap;
  va_start(ap, msg); 
  vsnprintf(buf, RB_BUFFER_LENGTH - 1, msg, ap);
  va_end(ap);

  time_t ltime;
  time(&ltime);
  struct tm *t = localtime(&ltime);
  fprintf(__log_fp, "[%d-%02d-%02d %02d:%02d:%02d]%c%s\n", 
          t->tm_year + 1900, t->tm_mon + 1, t->tm_mday, 
	  t->tm_hour, t->tm_min, t->tm_sec, ' ', buf);
  fflush(__log_fp);
}
