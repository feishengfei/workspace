#ifndef RBGLOBAL_H
#define RBGLOBAL_H

#define RB_EXPORT extern

#define LOG_LEVEL_WARNING

#ifdef __cplusplus
extern "C" {
#endif

RB_EXPORT void RB_DEBUG(const char *, ...)   /* print debug message */
#if __GNUC__ > 2 || (__GNUC__ == 2 && __GNUC_MINOR__ > 4)
  __attribute__ ((format (printf, 1, 2)))
#endif
;

RB_EXPORT void RB_WARNING(const char *, ...) /* print warning message */
#if __GNUC__ > 2 || (__GNUC__ == 2 && __GNUC_MINOR__ > 4)
  __attribute__ ((format (printf, 1, 2)))
#endif
;

RB_EXPORT void RB_FATAL(const char *, ...)   /* print fatal message and exit */
#if __GNUC__ > 2 || (__GNUC__ == 2 && __GNUC_MINOR__ > 4)
  __attribute__ ((format (printf, 1, 2)))
#endif
;

#if !defined(RB_ASSERT)
#  if !defined(NDEBUG)
#    define RB_ASSERT(x) ((x) ? (void)0 : RB_FATAL("ASSERT: \"%s\" in %s (%d)", #x, __FILE__, __LINE__))
#  else
#    define RB_ASSERT(x)
#  endif
#endif

RB_EXPORT void RB_LOG(const char *, ...)
#if __GNUC__ > 2 || (__GNUC__ == 2 && __GNUC_MINOR__ > 4)
  __attribute__ ((format (printf, 1, 2)))
#endif
;

#ifdef __cplusplus
}
#endif

#endif /* RBGLOBAL_H */
