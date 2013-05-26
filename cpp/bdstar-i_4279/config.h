#ifndef CONFIG_H
#define CONFIG_H

namespace BDI
{
    class ConfigGroup;
    
    class Config
    {
    private:
        char *_fileName;
        ConfigGroup *_grps;
        ConfigGroup *_curGroup;
        bool _dirty;
    
    private:
        void init();
        void setEntry(const char *key, const char *val);
    
        // no copys
        Config(const Config &);
        Config &operator=(const Config &);
    
    public:
        Config();
        Config(const char *fileName);
        ~Config();
    
        bool open(const char *fileName);
    
        bool save();
    
        bool isNull() const;
    
        void setGroup(const char *name);
    
        void setValue(const char *key, const char *val);
        void setValue(const char *key, int val);
        void setValue(const char *key, long val);
        void setValue(const char *key, double val);
    
        // The returned string is obtained with strdup(), and should 
        // be freed with free.
        char *getValue(const char *key, bool *ok = NULL);
        long getIntValue(const char *key, bool *ok = NULL);
        double getFloatValue(const char *key, bool *ok = NULL);
    };
    
    // Config inline functions
    
    inline bool Config::isNull() const
    { return _grps == NULL; }
};

#endif
