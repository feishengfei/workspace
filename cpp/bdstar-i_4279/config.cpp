#include <stdio.h>
#include <ctype.h>
#include <errno.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

#include "debug_alloc.h"
#include "config.h"

namespace BDI
{
    static char *normalize(const char *str)
    {
        char *res = STRDUP(str);
        if (res == NULL)
            return NULL;
    
        const char *p1 = str;
        char *p2 = res;
    
        for (; *p1 != '\0'; ++p1) {
            if (isspace(*p1)) {
                if (p2 != res && *(p2 - 1) != ' ')
                    *p2++ = ' ';
            }
            else
                *p2++ = *p1;
        }
    
        if (p2 != res && *(p2 - 1) == ' ')
            --p2;
        *p2 = '\0';
    
        return res;
    }
    
    struct ConfigEntry
    {
    public:
        char *key;
        char *val;
        ConfigEntry *next;
    };
    
    class ConfigGroup
    {
    private:
        char *_groupName;
        ConfigGroup *_sibling;
        ConfigEntry *_child;
        ConfigEntry *_lastEntry;
    
    private:
        ConfigEntry *findEntry(const char *key) const;
        ConfigEntry *addEntry(const char *key, const char *value);
    
    public:
        ConfigGroup(const char *name);
        ~ConfigGroup();
    
        const char *getValue(const char *key) const;
        void setValue(const char *key, const char *value);
    
        bool save(FILE *fp);
    
        friend class Config;
    };
    
    ConfigEntry *ConfigGroup::findEntry(const char *key) const
    {
        ConfigEntry *pent = _child;
        for (; pent != NULL; pent = pent->next) {
            if (pent->key != NULL && strcmp(pent->key, key) == 0)
                break;
        }
        return pent;
    }
    
    ConfigEntry *ConfigGroup::addEntry(const char *key, const char *value)
    {
        ConfigEntry *ent = NEW ConfigEntry;
        if (ent == NULL)
            return NULL;
        ent->key = normalize(key);
        ent->val = normalize(value);
        ent->next = NULL;
    
        if (_lastEntry == NULL) {
            assert(_child == NULL);
            _child = _lastEntry = ent;
        }
        else {
            _lastEntry->next = ent;
            _lastEntry = ent;
        }
    
        return ent;
    }
    
    ConfigGroup::ConfigGroup(const char *name)
        : _groupName(NULL), _sibling(NULL), _child(NULL), _lastEntry(NULL)
    {
        assert(name != NULL);
        _groupName = normalize(name);
    }
    
    ConfigGroup::~ConfigGroup()
    {
        if (_groupName != NULL)
            FREE(_groupName);
    
        ConfigEntry *pent = _child;
        ConfigEntry *next = NULL;
        for (; pent != NULL; pent = next) {
            next = pent->next;
            if (pent->key != NULL)
                FREE(pent->key);
            if (pent->val != NULL)
                FREE(pent->val);
            DELETE(pent);
        }
    }
    
    const char *ConfigGroup::getValue(const char *key) const
    {
        assert(key != NULL);
    
        const char *res = NULL;
        ConfigEntry *pent = findEntry(key);
        if (pent != NULL)
            res = pent->val;
        return res;
    }
    
    void ConfigGroup::setValue(const char *key, const char *value)
    {
        assert(key != NULL);
        assert(value != NULL);
    
        ConfigEntry *pent = findEntry(key);
        if (pent == NULL)
            addEntry(key, value);
        else {
            if (pent->val != NULL)
                FREE(pent->val);
            pent->val = normalize(value);
        }
    }
    
    bool ConfigGroup::save(FILE *fp)
    {
        assert(fp != NULL);
    
        clearerr(fp);
    
        fputc('[', fp);
        if (_groupName != NULL)
            fputs(_groupName, fp);
        fputs("]\n", fp);
    
        ConfigEntry *pent = _child;
        for (; pent != NULL && !ferror(fp); pent = pent->next) {
            if (pent->key != NULL && strlen(pent->key) > 0) {
                fputs(pent->key, fp);
                fputs(" = ", fp);
                if (pent->val != NULL && strlen(pent->val) > 0)
                    fputs(pent->val, fp);
                fputc('\n', fp);
            }
        }
    
        return !ferror(fp);
    }
};

using namespace BDI;

// Config members

void Config::init()
{
    _fileName = NULL;
    _grps = NULL;
    _curGroup = NULL;
    _dirty = false;
}

void Config::setEntry(const char *key, const char *val)
{
    if (_curGroup == NULL) {
        fprintf(stderr, "No current group setted.\n");
        return;
    }
    _curGroup->setValue(key, val);
}

Config::Config()
{
    init();
}

Config::Config(const char *fn)
{
    init();
    open(fn);
}

Config::~Config()
{
    if (_dirty)
        save();

    if (_fileName != NULL)
        FREE(_fileName);

    ConfigGroup *pgrp = _grps;
    ConfigGroup *next = NULL;
    for (; pgrp != NULL; pgrp = next) {
        next = pgrp->_sibling;
        DELETE(pgrp);
    }
}

bool Config::open(const char *fileName)
{
    assert(fileName != NULL);

    if (_fileName != NULL)
        FREE(_fileName);
    _fileName = STRDUP(fileName);

    FILE *fp = fopen(fileName, "r");
    if (fp == NULL) {
        fprintf(stderr, "Cann't open \"%s\": %s\n", fileName, strerror(errno));
        return false;
    }

    char *line = NEW char[1024];
    if (line == NULL) {
        perror("Lack of memory");
        return false;
    }

    char *p = NULL;
    while ((p = fgets(line, 1023, fp)) != NULL) {
        // truncates space in head
        for (; *p != '\0' && isspace(*p); ++p) ;

        if (*p == '[') { // group name
            char *grpnm = p + 1;
            for (; *p != '\0' && *p != ']'; ++p) ; // finding ']'
            if (*p == ']') {
                *p = '\0';
                grpnm = normalize(grpnm);
                if (grpnm != NULL) {
                    setGroup(grpnm);
                    FREE(grpnm);
                }
            }
        }
        else if (*p != '\0' && *p != '#') {
            // find the '=' or quotation mark
            const char *start = p;
            const char *key = NULL;
            const char *val = NULL;
            for (; *p != '\0' && *p != '='; ++p) ; // finding '='
            if (*p != '\0') {
                key = start;
                val = p + 1;
                *p = '\0';
                setEntry(key, val);
            }
        }
    }

    DELETE_ARR(line);
    fclose(fp);

    return true;
}

bool Config::save()
{
    FILE *fp = fopen(_fileName, "w");
    if (fp == NULL) {
        fprintf(stderr, "Cann't open \"%s\": %s\n", _fileName, strerror(errno));
        return false;
    }

    ConfigGroup *pgrp = _grps;
    bool ok = true;
    for (; pgrp != NULL && ok; pgrp = pgrp->_sibling)
        ok = pgrp->save(fp);

    _dirty = !ok;

    fclose(fp);
    return ok;
}

void Config::setGroup(const char *name)
{
    assert(name != NULL);

    ConfigGroup *pgrp = _grps;
    ConfigGroup *prev = NULL;
    while (pgrp != NULL) {
        if (pgrp->_groupName != NULL && strcmp(pgrp->_groupName, name) == 0)
            break;
        prev = pgrp;
        pgrp = pgrp->_sibling;
    }

    if (pgrp == NULL) {
        pgrp = NEW ConfigGroup(name);
        if (prev != NULL)
            prev->_sibling = pgrp;
        else
            _grps = pgrp;
    }

    _curGroup = pgrp;
}

void Config::setValue(const char *key, const char *val)
{
    setEntry(key, val);
    _dirty = true;
}

void Config::setValue(const char *key, int val)
{
    setValue(key, static_cast<long>(val));
}

void Config::setValue(const char *key, long val)
{
    char sbuf[16];
    sprintf(sbuf, "%ld", val);
    setValue(key, sbuf);
}

void Config::setValue(const char *key, double val)
{
    char sbuf[64];
    snprintf(sbuf, 63, "%g", val);
    setValue(key, sbuf);
}

char *Config::getValue(const char *key, bool *ok)
{
    char *res = NULL;

    if (ok != NULL)
        *ok = false;

    if (_curGroup == NULL) {
        fprintf(stderr, "No current group setted.\n");
        return res;
    }

    const char *val = _curGroup->getValue(key);
    if (val != NULL) {
        res = STRDUP(val);
        if (res == NULL)
            perror("Lack of memory");
        else if (ok != NULL)
            *ok = true;
    }

    return res;
}

long Config::getIntValue(const char *key, bool *ok)
{
    long res = 0;

    if (ok != NULL)
        *ok = false;

    if (_curGroup == NULL) {
        fprintf(stderr, "No current group setted.\n");
        return res;
    }

    const char *val = _curGroup->getValue(key);
    if (val != NULL) {
        char *endptr = NULL;
        res = strtol(val, &endptr, 10);
        if (*val != '\0' && *endptr == '\0') {
            if (ok != NULL)
                *ok = true;
        }
#ifndef NDEBUG
        else
            fprintf(stderr, "Bad integer value: \"%s\"\n", val);
#endif
    }

    return res;
}

double Config::getFloatValue(const char *key, bool *ok)
{
    double res = 0.0;

    if (ok != NULL)
        *ok = false;

    if (_curGroup == NULL) {
        fprintf(stderr, "No current group setted.\n");
        return res;
    }

    const char *val = _curGroup->getValue(key);
    if (val != NULL) {
        char *endptr = NULL;
        res = strtod(val, &endptr);
        if (*val != '\0' && *endptr == '\0') {
            if (ok != NULL)
                *ok = true;
        }
#ifndef NDEBUG
        else
            fprintf(stderr, "Bad floating value: \"%s\"\n", val);
#endif
    }

    return res;
}
