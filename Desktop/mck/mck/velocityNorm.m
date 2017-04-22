function ret=velocityNorm(c,n)
    
    v=Inf;
    tic
    t0=toc;
    while(v==Inf)
        r0 = [c.getFrame('rigidbody',n).RigidBody.x; c.getFrame('rigidbody',n).RigidBody.y; c.getFrame('rigidbody',n).RigidBody.z];
        if(toc-t0>0.1)
            rN=[c.getFrame('rigidbody',n).RigidBody.x; c.getFrame('rigidbody',n).RigidBody.y; c.getFrame('rigidbody',n).RigidBody.z];
            dR=rN-r0;
            dt=0.1;
            v=dR./dt; %coordinate distance traveled per second

            ret=[norm(v)];
            
        end
    end
end
