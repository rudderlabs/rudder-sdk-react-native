//
//  RNParamsConfigurator.h
//  Pods
//
//  Created by Abhishek Pandey on 02/08/23.
//

#ifndef RNParamsConfigurator_h
#define RNParamsConfigurator_h
#import "RSConfigBuilder.h"

@interface RNParamsConfigurator : NSObject {
    NSDictionary *config;
    RSDBEncryption *dbEncryption;
}

@property (nonatomic, assign) BOOL trackLifeCycleEvents;
@property (nonatomic, assign) BOOL recordScreenViews;
@property (nonatomic, assign) long sessionTimeout;
@property (nonatomic, assign) BOOL autoSessionTracking;
@property (nonatomic, assign) BOOL enableBackgroundMode;
@property (nonatomic, strong) NSString* writeKey;

- (instancetype) initWithConfig:(NSDictionary*)config;
-(RSConfigBuilder*) handleConfig;

@end


#endif /* RNParamsConfigurator_h */
