//
//  Person.h
//  FirstApp
//
//  Created by Frank Zhou on 12-7-17.
//  Copyright (c) 2012年 Liteon. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Person : NSObject

-(void)SimpleMethod;

@property int age;
@property (nonatomic,strong) NSString *name;
@end
