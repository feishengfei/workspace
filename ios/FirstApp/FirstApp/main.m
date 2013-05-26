//
//  main.m
//  FirstApp
//
//  Created by Frank Zhou on 12-7-15.
//  Copyright (c) 2012年 Liteon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Person.h"

int main (int argc, const char * argv[])
{
    
    @autoreleasepool {
        int dayInAYear = 365;
        int hour = 24;
        int minute = 60;
        int minutesInAYear = dayInAYear * hour * minute;
        // insert code here...
        NSLog(@"Hello, World!");
        switch (hour) {
            case 1:
                //  
                break;
                
            default:
                break;
        }
        NSLog(@"There're %i minutes in a year", minutesInAYear);
        Person *p = [[Person alloc]init];
        [p SimpleMethod];
        [p setAge:18];
        [p setName:@"Frank"];
        NSLog(@"%@ 今年 %i 岁了", [[p name] uppercaseString] ,[p age]);
        
        NSDate *now = [NSDate new];
        NSLog(@"now(UTC) is %@", [now description]);
        NSLocale *locale = [NSLocale systemLocale];
        NSLog(@"now(locale) is %@", [now descriptionWithLocale:locale]);
        
        now = nil;
        NSLog(@"now(UTC) is %@", [now description]);
        NSLog(@"%@",[NSString stringWithFormat:@"abc%i",100]);
    }
    return 0;
}

