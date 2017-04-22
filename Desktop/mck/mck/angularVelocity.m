function ret=angularVelocity(c,n1,n2,n3)
    
    %RB numbers need to be entered in order hip/shoulder, foot/hand,
    %knee/elbow

    theta = Inf;
    %sets theta to inf to be bale to loop until the desired time step has
    %passed
    
    %angle between vectors, based on the geometrical definitions of the
    %scalar and vector products            
    AngleBetweenVectors=@(u,v) atan2d(norm(cross(u,v)),dot(u,v));
    
    tic
    t0=toc;    
    while(theta==Inf)
        
        %Matrix with position vectors as columns of the first frame
        A=RBPosVector(n1,n2,n3,c);
        
        %calculate angle between vectors formed at the extremity
        thetaL1=180-AngleBetweenVectors(A(1:3,3)-A(1:3,1),A(1:3,2)-A(1:3,3));
        if(toc-t0>0.1)
            %time step has passed
            B=RBPosVector(n1,n2,n3,c);            
            thetaL2=180-AngleBetweenVectors(B(1:3,3)-B(1:3,1),B(1:3,2)-B(1:3,3));
            %get new angle
            dTheta=thetaL1-thetaL2;
            %angle difference
            dt=0.1;
            ret = dTheta/dt;
        end
    
    
    end
    
    
end