include $(top_srcdir)/build/modmake.rulesam
MODNAME=mod_pdt

mod_LTLIBRARIES = mod_pdt.la
mod_pdt_la_SOURCES  = mod_pdt.c	pdt_api.c pdt_cmd.c pdt_event.c win_iconv.c
mod_pdt_la_CFLAGS   = $(AM_CFLAGS)
mod_pdt_la_LIBADD   = $(switch_builddir)/libfreeswitch.la
mod_pdt_la_LDFLAGS  = -avoid-version -module -no-undefined -shared
